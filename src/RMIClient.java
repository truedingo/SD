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
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("0. Quit");
                System.out.print("> Opcao: ");

                Scanner s = new Scanner(System.in);
                String strOpt = s.nextLine();
                int opt = Integer.parseInt(strOpt);

                //verificacao option
                if ((opt < 0) || (opt > 3)) {
                    System.out.println("\tInvalid option! ");
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
            menuUser(username);
        }
        else{
            System.out.println("Username already in use.");
        }


    }

    public static void login() throws RemoteException{
        Scanner s = new Scanner(System.in);
        System.out.println("Username: ");
        String username = s.nextLine();

        System.out.println("Password: ");
        String password = s.nextLine();

        if(rmiInterface.checkLogin(username, password).equals("editor")){
            System.out.println("Logged in as editor.");
            menuAdministrador();
        }
        else if(rmiInterface.checkLogin(username, password).equals("user")){
            System.out.println("Logged in as user.");
            menuUser(username);
        }
        else{
            System.out.println("Error with login.");
        }
    }

    //----------- funcao de menu administrador -------------------
    public static void menuAdministrador(){
        while (true) {
            try {
                System.out.println("\t- Administrator Menu -");
                System.out.println("1. Insert data");
                System.out.println("2. Change data");
                System.out.println("3. Remove data");
                System.out.println("4. Give editor rights ");
                System.out.println("0. Quit");

                Scanner s = new Scanner(System.in);
                String strOpt = s.nextLine();
                int opt = Integer.parseInt(strOpt);

                System.out.print("\n> Option: ");
                //verificacao option
                if ((opt < 0) || (opt > 4)) {
                    System.out.println("\n\tInvalid option! ");
                    continue;
                }

                switch (opt) {
                    case 1:
                        //menu insert data
                        insertData();
                        return;
                    case 2:
                        //menu change data
                        changeData();
                        break;
                    case 3:
                        //menu remove data
                        removeData();
                    case 4:
                        //change user rights
                        changeRights();
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

    //-------------- menu remove data -----------------------
    public static void removeData(){
        while (true){
            try {
                System.out.println("\n\t- Remove Data -");
                System.out.println("\n1. Remove music.");
                System.out.println("\n2. Remove artist.");
                System.out.println("\n3. Remove album.");
                System.out.println("\n0. Quit");
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
                        //remove musica
                        //login();
                        return;
                    case 2:
                        //remove artista
                        register();
                        break;
                    case 3 :
                        //remove album
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

    //-------------- menu change data -----------------------
    public static void changeData(){
        while (true){
            try {
                System.out.println("\n\t- Change Data -");
                System.out.println("\n1. Change music data.");
                System.out.println("\n2. Change artist data.");
                System.out.println("\n3. Change album data.");
                System.out.println("\n0. Quit");
                System.out.print("\n> Option: ");

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
                        //mudar dados da musica
                        return;
                    case 2:
                        //mudar dados de artista
                        //
                        break;
                    case 3 :
                        //mudar dados de album
                    case 0:
                        System.exit(0);
                        return;
                }

            }catch (NumberFormatException e ){
                System.out.println("Invalid option.");
            } catch (NoSuchElementException e){}
        }
    }

    //-------------- menu insert data -----------------------
    public static void insertData(){
        while (true){
            try {
                System.out.println("\n\t- Insert Data -");
                System.out.println("\n1. Insert music.");
                System.out.println("\n2. Insert artist.");
                System.out.println("\n3. Insert album.");
                System.out.println("\n0. Quit");
                System.out.print("\n> Option: ");

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
                        insertMusic();
                        //TODO
                        return;
                    case 2:
                        //insert artist
                        break;
                    case 3 :
                        //insert album

                    case 0:
                        System.exit(0);
                        return;
                }

            }catch (NumberFormatException e ){
                System.out.println("Invalid option.");
            } catch (NoSuchElementException e){}
        }
    }

    //------------ insert music -----------------------

    public static void insertMusic(){
        System.out.println("\n\t- Insert Music -");
        System.out.println("\nMusic name:");
        Scanner musicName = new Scanner(System.in);
        String strName = musicName.nextLine();

        System.out.println("\nMusic genre:");
        String strGenre = musicName.nextLine();

        System.out.println("\nMusic duration:");
        String strDuration = musicName.nextLine();


    }

    //---------- menu user ---------//
    public static void menuUser(String username){

        while (true) {
            try {
                System.out.println("\n\t- User Menu -");
                System.out.println("\n1. View Data");
                System.out.println("\n0. Quit");

                Scanner s = new Scanner(System.in);
                String strOpt = s.nextLine();
                int opt = Integer.parseInt(strOpt);

                System.out.print("\n> Option: ");
                //verificacao option
                if ((opt < 0) || (opt > 3)) {
                    System.out.println("\n\tInvalid option! ");
                    continue;
                }

                switch (opt) {
                    case 1:
                        //menu insert data
                        return;
                    case 2:
                        //menu change data
                        break;
                    case 3:
                        //menu remove data
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

    //--------change user rights-------
    public static void changeRights() throws RemoteException {
        Scanner s = new Scanner(System.in);
        System.out.println("Insert username: ");
        String username = s.nextLine();
        if(rmiInterface.checkUserRights(username)){
            System.out.println("Changed rights of user "+username+" to editor.");
            menuAdministrador();
        }
        else{
            System.out.println("User not found or is already an editor.");
            menuAdministrador();
        }



    }
}