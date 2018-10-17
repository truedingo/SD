import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class RMIClient {
    private static RMI rmiInterface =null;
    private static Configurations configurations;

    public static void register() throws RemoteException{
        Scanner s = new Scanner(System.in);
        try {
            System.out.print("\n> Username: ");
            String username = s.nextLine();

            System.out.print("\n> Password: ");
            String password = s.nextLine();

            User u = new User(username,password,false,false,false,false);

            if(rmiInterface.verificaUserExiste(u.getUsername())) {
                rmiInterface.insertUser(u);
                System.out.println("Success! [Register]");
            }
            else {
                System.out.println("User already in use. Try again!");
                register();
            }
        } catch(ConnectException e) {
            System.out.println("RMI Server inactive.");
        }
    }


    public static void login() throws RemoteException {
        Scanner s = new Scanner(System.in);
        System.out.print("\n> Username: ");

        String username = s.nextLine();

        System.out.print("\n> Password: ");
        String password = s.nextLine();

        if(rmiInterface.verifyCredencials(username,password)){
            System.out.println("Welcome to DropMusic");
        }
        else {
            System.out.println("Wrong credencials, try again.");
            login();
        }
    }


        public static void menuLogin() throws RemoteException{
        Scanner s = new Scanner(System.in);

        while (true){
            try {
                System.out.println("\n\t- Welcome to DropMusic -");
                System.out.println("\n1. Login");
                System.out.println("\n2. Register");
                System.out.println("\n0. Quit");
                System.out.print("\n> Opcao: ");

                String strOpt = s.nextLine();
                int opt = Integer.parseInt(strOpt);

                //verificacao option
                if ((opt < 0) || (opt > 3)) {
                    System.out.println("\n\tInvalid option! ");
                    continue;
                }

                switch (opt) {
                    case 1:
                        //fuction login
                        login();
                        return;
                    case 2:
                        //function register
                        register();
                        break;
                    case 0:
                        System.exit(0);
                        return;
                }

            }catch (NumberFormatException e ){
                System.out.println("Invalid option.");
            } catch (NoSuchElementException e){}
        }
    }

    public static void main(String args[]) throws InterruptedException {

        //This might be necessary if you ever need to download classes:
        //System.getProperties().put("java.security.policy", "policy.all");
        //System.setSecurityManager(new RMISecurityManager());
        //configurations = new Configurations(("/Users/iroseiro/IdeaProjects/Project/src/com/company/RMI_configs.cfg"));
        configurations = new Configurations(("/Users/dingo/Desktop/SD/DropMusicMerged/src/RMI_configs.cfg"));

        try {
            RMI rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
            rmiInterface.sayHello();
            menuLogin();

        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
            e.printStackTrace();
        }

    }
}