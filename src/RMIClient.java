import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RemoteRef;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class RMIClient {
    private static Configurations configurations;
    private static RMI rmiInterface;

    //validate date in format dd/mm/yyyy or d/m/yyyy
    private static final DateTimeFormatter PARSE_FORMATTER = DateTimeFormatter.ofPattern("d/M/uuuu");

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
                System.out.println("1. Remove artist.");
                System.out.println("2. Remove album.");
                System.out.println("3. Remove music.");
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
                        //remove artist
                        removeArtist();
                        return;
                    case 2:
                        //remove album
                        removeAlbum();
                        return;
                    case 3 :
                        //remove music
                        removeMusic();
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

    //menu change data
    public static void changeData(){
        while (true){
            try {
                System.out.println("\n\t- Change Data -");
                System.out.println("1. Change artist data.");
                System.out.println("2. Change album data.");
                System.out.println("3. Change music data.");
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
                        //mudar dados da artista
                        editArtist();
                        return;
                    case 2:
                        //mudar dados de album
                        editAlbum();
                        //
                        return;
                    case 3 :
                        //mudar dados de musica
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
        System.out.println("Music name:");
        Scanner s = new Scanner(System.in);
        String strName = s.nextLine();
        System.out.println("Music genre:");
        String strGenre = s.nextLine();

        System.out.println("Music duration:");
        String strDuration = s.nextLine();

        String udate = dateInput(s);

        System.out.println("Lyrics:");
        String lyrics = s.nextLine();

        System.out.println("Artist name:");
        String strArtistName = s.nextLine();

        System.out.println("Album name:");
        String strAlbumName = s.nextLine();


        //CheckMusic
        try {
            if (rmiInterface.checkMusic(strName, strGenre,strDuration,udate, lyrics, strArtistName,strAlbumName)) {

                System.out.println("Music added.");
                menuAdministrador();
            } else {
                System.out.println("Error adding music.");
                menuAdministrador();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    //insert artist
    public static void insertArtist() throws RemoteException {
        System.out.println("\n\t- Insert Artist -");
        System.out.println("Artist name:");
        Scanner s = new Scanner(System.in);

        String artistName = s.nextLine();
        System.out.println("Artist description:");
        String artistDesc = s.nextLine();

        if (rmiInterface.checkArtist(artistName, artistDesc)) {
            System.out.println("Artist added.");
            menuAdministrador();
        } else {
            System.out.println("Error adding artist.");
            menuAdministrador();
        }
    }

    //insert album
    public static void insertAlbum() throws RemoteException {

        System.out.println("\n\t- Insert Album -");
        System.out.println("Album name:");
        Scanner s = new Scanner(System.in);

        String albumName = s.nextLine();
        System.out.println("Album description:");
        String albumDescr = s.nextLine();

        System.out.println("Musical Genre:");
        String musicalGenre = s.nextLine();

        String udate = dateInput(s);

        System.out.println("Artist name:");
        String artistName = s.nextLine();


        if (rmiInterface.checkAlbum(albumName, albumDescr, musicalGenre, udate, artistName)) {
            System.out.println("Album added.");
            menuAdministrador();
        } else {
            System.out.println("Error adding Album.");
            menuAdministrador();

        }
    }


    //-------- REMOVE FUNCTIONS--------//

    //remove music
    public static void removeMusic() throws RemoteException{
        System.out.println("\n\t- Remove Music -");
        Scanner s = new Scanner(System.in);

        System.out.println("Music name:");
        String musicName = s.nextLine();
        System.out.println("Artist name:");
        String artistName = s.nextLine();
        System.out.println("Album name:");
        String albumName = s.nextLine();

        if (rmiInterface.checkRemoveMusic(musicName,artistName,albumName)) {
            System.out.println("Music removed.");
            menuAdministrador();
        } else {
            System.out.println("Error removing music.");
            menuAdministrador();

        }
    }

    //remove album
    public static void removeAlbum() throws RemoteException{
        System.out.println("\n\t- Remove Album -");
        Scanner s = new Scanner(System.in);

        System.out.println("Album name:");
        String albumName = s.nextLine();
        System.out.println("Artist name:");
        String artistName = s.nextLine();


        if (rmiInterface.checkRemoveAlbum(artistName,albumName)) {
            System.out.println("Album removed.");
            menuAdministrador();
        } else {
            System.out.println("Error removing album.");
            menuAdministrador();

        }
    }

    //remove artist
    public static void removeArtist() throws RemoteException{
        System.out.println("\n\t- Remove Artist -");
        Scanner s = new Scanner(System.in);

        System.out.println("Artist name:");
        String artistName = s.nextLine();

        if (rmiInterface.checkRemoveArtist(artistName)) {
            System.out.println("Artist removed.");
            menuAdministrador();
        } else {
            System.out.println("Error removing artist.");
            menuAdministrador();

        }
    }


    //-------- EDIT FUNCTIONS--------//

    public static void editMusic() throws RemoteException {
        System.out.println("\n\t- Edit Music -");
        System.out.println("\n\tINPUT OLD DATA");

        System.out.println("Artist name of music to edit: ");
        Scanner s = new Scanner(System.in);
        String artistName = s.nextLine();

        System.out.println("Album name of music to edit:");
        String albumName = s.nextLine();

        System.out.println("Music name to edit:");
        String oldMusicName = s.nextLine();

        System.out.println("\n\t INPUT NEW DATA");
        System.out.println("NEW Music name: ");
        String newMusicName = s.nextLine();

        System.out.println("NEW Music genre:");
        String strGenre = s.nextLine();

        System.out.println("NEW Music duration:");
        String strDuration = s.nextLine();

        String udate = dateInput(s);

        System.out.println("NEW Lyrics:");
        String lyrics = s.nextLine();

        try {
            if (rmiInterface.checkEditSong(artistName, albumName,oldMusicName,newMusicName, strGenre, strDuration, udate, lyrics)) {

                System.out.println("Music edited.");
                menuAdministrador();
            } else {
                System.out.println("Error editing music.");
                menuAdministrador();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    //insert artist
    public static void editArtist() throws RemoteException {
        System.out.println("\n\t- Edit Artist -");
        System.out.println("\n\tINPUT OLD DATA");

        System.out.println("Artist name to edit:");
        Scanner s = new Scanner(System.in);
        String oldArtistName = s.nextLine();

        System.out.println("\n\t INPUT NEW DATA");

        System.out.println("NEW Artist name:");
        String newArtistName = s.nextLine();

        System.out.println("NEW Artist description:");
        String artistDesc = s.nextLine();

        if (rmiInterface.checkEditArtist(oldArtistName, newArtistName, artistDesc)) {
            System.out.println("Artist edited.");
            menuAdministrador();
        } else {
            System.out.println("Error editing artist.");
            menuAdministrador();
        }
    }

    //insert album
    public static void editAlbum() throws RemoteException {
        System.out.println("\n\t- Edit Album -");
        System.out.println("\n\tINPUT OLD DATA");

        System.out.println("Artist name of album to edit: ");
        Scanner s = new Scanner(System.in);
        String artistName = s.nextLine();

        System.out.println("Album name to edit:");
        String oldAlbumName = s.nextLine();

        System.out.println("\n\t INPUT NEW DATA");

        System.out.println("NEW Album name:");
        String newAlbumName = s.nextLine();

        System.out.println("NEW Album description:");
        String albumDescr = s.nextLine();

        System.out.println("NEW Musical Genre:");
        String musicalGenre = s.nextLine();

        String udate = dateInput(s);


        if (rmiInterface.checkEditAlbum(artistName, oldAlbumName, newAlbumName, albumDescr, musicalGenre, udate)) {
            System.out.println("Album edited.");
            menuAdministrador();
        } else {
            System.out.println("Error editing album.");
            menuAdministrador();

        }
    }

    //-------OTHERS----------//

    ///https://stackoverflow.com/questions/44696400/user-to-enter-date-mm-dd-yyyy-and-validate-the-data-entered
    public static String dateInput(Scanner s) {
        System.out.println("Please enter a date (mm/dd/yyyy)");
        String uDate = s.nextLine();
        try {
            LocalDate.parse(uDate, PARSE_FORMATTER);
        } catch (DateTimeParseException dtpe) {
            System.out.println(uDate + " is a not valid Date");
            dateInput(s);
        }
        return uDate;
    }



}

