import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class RMIClient {
    private static Configurations configurations;
    private static RMI rmiInterface;

    public static void welcome(){
        while (true){
            try {
                System.out.println("\n\t- Welcome to DropMusic -");
                System.out.println("\n1. Login");
                System.out.println("\n2. Register");
                System.out.println("\n0. Quit");
                System.out.print("\n> Opcao: ");

                Scanner s = new Scanner(System.in);
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
                        //login();
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
            } catch (NoSuchElementException e){} catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws InterruptedException {

        //This might be necessary if you ever need to download classes:
        //System.getProperties().put("java.security.policy", "policy.all");
        //System.setSecurityManager(new RMISecurityManager());
        //configurations = new Configurations(("/Users/iroseiro/IdeaProjects/Project/src/com/company/RMI_configs.cfg"));
        configurations = new Configurations("/Users/dingo/Desktop/SD/DropMusicMerged/out/production/DropMusicMerged/RMI_configs.cfg");

        try {
            rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
            rmiInterface.sayHello();
            welcome();

        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
            e.printStackTrace();
        }

    }

    public static void register() throws RemoteException{

        Scanner s = new Scanner(System.in);
        System.out.println("Username: ");
        String username = s.nextLine();

        System.out.println("Password: ");
        String password = s.nextLine();

        if(rmiInterface.checkRegister(username, password)){
            System.out.println("Registered successfully.");
        }
        else{
            System.out.println("Username already in use.");
        }


    }
}