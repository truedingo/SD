import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RemoteRef;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class RMIClient {
    private static Configurations configurations;
    private static RMI rmiInterface;

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


    //-------- MENUS--------//

    //menu welcome
    public static void welcome(){
        while (true){
            try {
                System.out.println("\n\t- Welcome to DropMusic -");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("0. Quit");
                System.out.print("> Option: ");

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
                        return;
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
    //menu user
    public static void menuUser(String username){

        while (true) {
            try {
                if(rmiInterface.idleUserRights(username)){
                    System.out.println("Your rights were changed to editor.");
                    menuAdministrador();
                }
                System.out.println("\t- User Menu -");
                System.out.println("1. View Data");
                System.out.println("0. Quit");

                Scanner s = new Scanner(System.in);
                String strOpt = s.nextLine();
                int opt = Integer.parseInt(strOpt);
                //verificacao option
                if ((opt < 0) || (opt > 4)) {
                    System.out.println("\tInvalid option! ");
                    continue;
                }

                switch (opt) {
                    case 1:
                        menuUser(username);
                        return;
                    case 2:
                        //menu change data
                        return;
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

    //menu admin
    public static void menuAdministrador(){
        while (true) {
            try {
                System.out.println("\n\t- Administrator Menu -");
                System.out.println("1. Insert data");
                System.out.println("2. Change data");
                System.out.println("3. Remove data");
                System.out.println("4. Give editor rights ");
                System.out.println("0. Quit");

                Scanner s = new Scanner(System.in);
                String strOpt = s.nextLine();
                int opt = Integer.parseInt(strOpt);
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
                        //here
                        changeData();
                        return;
                    case 3:
                        //menu remove data
                        removeData();
                        return;
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

    //menu remove data
    public static void removeData(){
        while (true){
            try {
                System.out.println("\n\t- Remove Data -");
                System.out.println("1. Remove music.");
                System.out.println("2. Remove artist.");
                System.out.println("3. Remove album.");
                System.out.println("4. Return to Admin Menu");
                System.out.println("0. Quit");
                Scanner s = new Scanner(System.in);
                String strOpt = s.nextLine();
                int opt = Integer.parseInt(strOpt);

                //verificacao option
                if ((opt < 0) || (opt > 4)) {
                    System.out.println("\tInvalid option! ");
                    continue;
                }

                switch (opt) {
                    case 1:
                        //remove musica
                        return;
                    case 2:
                        //remove artista
                        return;
                    case 3 :
                        //remove album
                    case 4:
                        menuAdministrador();
                        return;
                    case 0:
                        System.exit(0);
                        return;
                }

            }catch (NumberFormatException e ){
                System.out.println("Invalid option.");
            } catch (NoSuchElementException e){}
        }
    }

    //menu change data
    public static void changeData(){
        while (true){
            try {
                System.out.println("\n\t- Change Data -");
                System.out.println("1. Change music data.");
                System.out.println("2. Change artist data.");
                System.out.println("3. Change album data.");
                System.out.println("4. Return to Admin Menu");
                System.out.println("0. Quit");

                Scanner s = new Scanner(System.in);
                String strOpt = s.nextLine();
                int opt = Integer.parseInt(strOpt);

                //verificacao option
                if ((opt < 0) || (opt > 4)) {
                    System.out.println("\tInvalid option! ");
                    continue;
                }

                switch (opt) {
                    case 1:
                        //mudar dados da musica
                        return;
                    case 2:
                        //mudar dados de artista
                        //
                        return;
                    case 3 :
                        //mudar dados de album
                    case 4:
                        menuAdministrador();
                        return;
                    case 0:
                        System.exit(0);
                        return;
                }

            }catch (NumberFormatException e ){
                System.out.println("Invalid option.");
            } catch (NoSuchElementException e){}
        }
    }

    //menu insert data
    public static void insertData(){
        while (true){
            try {
                System.out.println("\t- Insert Data -");
                System.out.println("1. Insert artist.");
                System.out.println("2. Insert album.");
                System.out.println("3. Insert music.");
                System.out.println("4. Return to Admin Menu");
                System.out.println("0. Quit");

                Scanner s = new Scanner(System.in);
                String strOpt = s.nextLine();
                int opt = Integer.parseInt(strOpt);

                //verificacao option
                if ((opt < 0) || (opt > 4)) {
                    System.out.println("\tInvalid option! ");
                    continue;
                }

                switch (opt) {
                    case 1:
                        insertArtist();
                        return;
                    case 2:
                        insertAlbum();
                        return;
                    case 3 :
                        insertMusic();
                        return;
                    case 4:
                        menuAdministrador();
                        return;
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


    //-------- MENU FUNCTIONS--------//

    //register
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
            welcome();
        }


    }

    //login
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
            welcome();
        }
    }

    //change rights
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


    //-------- INSERT FUNCTIONS--------//

    //insert music
    public static void insertMusic() throws RemoteException {
        System.out.println("\n\t- Insert Music -");
        System.out.println("\nMusic name:");
        Scanner s = new Scanner(System.in);
        String strName = s.nextLine();

        System.out.println("\nMusic genre:");
        String strGenre = s.nextLine();

        System.out.println("\nMusic duration:");
        String strDuration = s.nextLine();

        System.out.println("\nArtist name:");
        String strArtistName = s.nextLine();

        System.out.println("\nAlbum name:");
        String strAlbumName = s.nextLine();


        //CheckMusic
        try {
            if (rmiInterface.checkMusic(strName, strGenre,strDuration,strArtistName,strAlbumName)) {

                System.out.println("Music added.");
                menuAdministrador();
            } else {
                System.out.println("Music already exists! Try again");
                menuAdministrador();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    //insert artist
    public static void insertArtist() throws RemoteException {
        System.out.println("\n\t- Insert Artist -");
        System.out.println("\nArtist name:");
        Scanner s = new Scanner(System.in);

        String artistName = s.nextLine();
        System.out.println("\nArtist description:");
        String artistDesc = s.nextLine();

        if (rmiInterface.checkArtist(artistName, artistDesc)) {
            System.out.println("Artist added.");
            menuAdministrador();
        } else {
            System.out.println("Artist already exists! Try again");
            menuAdministrador();
        }
    }

    //insert album
    public static void insertAlbum() throws RemoteException {
        System.out.println("\n\t- Insert Album -");
        System.out.println("\nAlbum name:");
        Scanner s = new Scanner(System.in);

        String albumName = s.nextLine();
        System.out.println("\nAlbum description:");
        String albumDescr = s.nextLine();

        System.out.println("\nArtist:");
        String artistName = s.nextLine();


        if (rmiInterface.checkAlbum(albumName, albumDescr,artistName)) {
            System.out.println("Album added.");
            menuAdministrador();
        } else {
            System.out.println("Album already exists! Try again");
            menuAdministrador();

        }
    }

    //-------- REMOVE FUNCTIONS--------//

    //remove music
    public static void removeMusic() throws RemoteException{
        System.out.println("\n\t- Remove Music -");
        Scanner s = new Scanner(System.in);

        System.out.println("\nMusic name:");
        String musicName = s.nextLine();
        System.out.println("\nArtist name:");
        String artistName = s.nextLine();
        System.out.println("\nAlbum name:");
        String albumName = s.nextLine();

        if (rmiInterface.checkRemoveMusic(musicName,artistName,albumName)) {
            System.out.println("Music removed.");
            menuAdministrador();
        } else {
            System.out.println("Music cannot be removed. Try again!");
            removeMusic();

        }
    }

    //remove album
    public static void removeAlbum() throws RemoteException{
        System.out.println("\n\t- Remove Album -");
        Scanner s = new Scanner(System.in);

        System.out.println("\nAlbum name:");
        String albumName = s.nextLine();
        System.out.println("\nArtist name:");
        String artistName = s.nextLine();


        if (rmiInterface.checkRemoveAlbum(artistName,albumName)) {
            System.out.println("Album removed.");
            menuAdministrador();
        } else {
            System.out.println("Album cannot be removed. Try again!");
            removeAlbum();

        }
    }

    //remove artist
    public static void removeArtist() throws RemoteException{
        System.out.println("\n\t- Remove Artist -");
        Scanner s = new Scanner(System.in);

        System.out.println("\nArtist name:");
        String artistName = s.nextLine();

        if (rmiInterface.checkRemoveArtist(artistName)) {
            System.out.println("Artist removed.");
            menuAdministrador();
        } else {
            System.out.println("Artist cannot be removed. Try again!");
            removeArtist();

        }
    }

}

