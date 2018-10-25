import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class RMIClient extends UnicastRemoteObject implements ClientInterface{
    private static Configurations configurations;
    private static RMI rmiInterface;
    private static String clientUsername;
    private static RMIClient client;

    //validate date in format dd/mm/yyyy or d/m/yyyy
    private static final DateTimeFormatter PARSE_FORMATTER = DateTimeFormatter.ofPattern("d/M/uuuu");

    RMIClient() throws RemoteException {
        super();
    }

    public static void main(String args[]) throws InterruptedException {

        //This might be necessary if you ever need to download classes:
        System.getProperties().put("java.security.policy", "policy.all");
        System.setSecurityManager(new RMISecurityManager());
        //configurations = new Configurations(("/Users/iroseiro/IdeaProjects/Project/src/com/company/RMI_configs.cfg"));
        configurations = new Configurations("/Users/dingo/Desktop/SD/DropMusicMerged/out/production/DropMusicMerged/RMI_configs.cfg");

        try {
            rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
            rmiInterface.sayHello();
            client = new RMIClient();
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
    public static void menuUser(String username) throws RemoteException {

        while (true) {
            if(rmiInterface.checkNotificationsRights(username, client)){
                menuAdministrador(username);
            }
            System.out.println("\t- User Menu -");
            System.out.println("1. View Data");
            System.out.println("2. Write Critic");
            System.out.println("0. Log Out");
            Scanner s = new Scanner(System.in);
            String strOpt = s.nextLine();
            int opt = Integer.parseInt(strOpt);
            //verificacao option
            if ((opt < 0) || (opt > 2)) {
                System.out.println("\tInvalid option!");
                continue;
            }

            switch (opt) {
                case 1:
                    menuUser(username);
                    return;
                case 2:
                    writeCritic(username);
                    return;
                case 0:
                    System.out.println("Logged out!");
                    //remover
                    String logout = rmiInterface.removeLoggedUsers(username);
                    if(logout.equals("deleted")){
                        System.exit(0);
                    }
                    else{
                        System.out.println("és um burro diogo");
                    }
                    return;
            }
        }
    }

    //menu admin
    public static void menuAdministrador(String username) throws RemoteException {
        while (true) {
            rmiInterface.checkNotificationsRights(username, client);
            try {
                System.out.println("\n\t- Administrator Menu -");
                System.out.println("1. Insert data");
                System.out.println("2. Change data");
                System.out.println("3. Remove data");
                System.out.println("4. Give editor rights ");
                System.out.println("0. Log Out");

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
                        insertData(username);
                        return;
                    case 2:
                        //menu change data
                        //here
                        changeData(username);
                        return;
                    case 3:
                        //menu remove data
                        removeData(username);
                        return;
                    case 4:
                        //change user rights
                        changeRights(username);
                    case 0:
                        System.out.println("Logged out!");
                        //remover
                        String logout = rmiInterface.removeLoggedUsers(username);
                        if(logout.equals("deleted")){
                            System.exit(0);
                        }
                        else{
                            System.out.println("és um burro diogo");
                        }
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
    public static void removeData(String username){
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
                        removeArtist(username);
                        return;
                    case 2:
                        //remove album
                        removeAlbum(username);
                        return;
                    case 3 :
                        //remove music
                        removeMusic(username);
                        return;
                    case 4:
                        menuAdministrador(username);
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
    public static void changeData(String username){
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
                        editArtist(username);
                        return;
                    case 2:
                        //mudar dados de album
                        editAlbum(username);
                        //
                        return;
                    case 3 :
                        //mudar dados de musica
                        editMusic(username);
                    case 4:
                        menuAdministrador(username);
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
    public static void insertData(String username){
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
                        insertArtist(username);
                        return;
                    case 2:
                        insertAlbum(username);
                        return;
                    case 3 :
                        insertMusic(username);
                        return;
                    case 4:
                        menuAdministrador(username);
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
            clientUsername = username;
            client.setUsername(clientUsername);
            //adicionar a lista de logged users no RMI
            rmiInterface.addLoggedUsers(client);
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

            //meter interface com nome de user
            clientUsername = username;
            client.setUsername(clientUsername);
            //adicionar a lista de logged users no RMI
            rmiInterface.addLoggedUsers(client);

            menuAdministrador(username);
        }
        else if(rmiInterface.checkLogin(username, password).equals("user")){
            System.out.println("Logged in as user.");

            clientUsername = username;
            client.setUsername(clientUsername);
            //adicionar a lista de logged users no RMI
            rmiInterface.addLoggedUsers(client);

            menuUser(username);
        }
        else{
            System.out.println("Error with login.");
            welcome();
        }
    }

    //change rights
    public static void changeRights(String user) throws RemoteException {
        Scanner s = new Scanner(System.in);
        System.out.println("Insert username: ");
        String username = s.nextLine();

        boolean checker = rmiInterface.checkUserRights(username, client);
        if(checker){
            System.out.println("Changed rights of user "+username+" to editor.");
            menuAdministrador(user);
        }
        else{
            System.out.println("User not found or is already an editor.");
            menuAdministrador(user);
        }
    }

    //write critic
    public static void writeCritic(String username) {
        System.out.println("\n\t- Write Critic -");
        Scanner s = new Scanner(System.in);

        System.out.println("Artist name:");
        String strArtistName = s.nextLine();

        System.out.println("Album name:");
        String strAlbumName = s.nextLine();

        Scanner sc = new Scanner(System.in);
        System.out.println("Rate album (0-5):");
        int rate = sc.nextInt();

        if ((rate < 0) || (rate > 5)) {
            System.out.println("\tInvalid rate! Try again ");
            writeCritic(username);
        }
        System.out.println("Write critic:");
        String strCritic = s.nextLine();

        if (strCritic.length()>300) {
            System.out.println("\tCritic too big! Try again");
            writeCritic(username);
        }

        try {
            if (rmiInterface.checkCritic(username,strArtistName,strAlbumName,rate,strCritic)) {
                System.out.println("Critic added.");
                menuUser(username);
            } else {
                System.out.println("Error with critic! Try again");
                menuUser(username);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }



    //-------- INSERT FUNCTIONS--------//

    //insert music
    public static void insertMusic(String username) {
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
                menuAdministrador(username);
            } else {
                System.out.println("Error adding music.");
                menuAdministrador(username);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    //insert artist
    public static void insertArtist(String username) throws RemoteException {
        System.out.println("\n\t- Insert Artist -");
        System.out.println("Artist name:");
        Scanner s = new Scanner(System.in);

        String artistName = s.nextLine();
        System.out.println("Artist description:");
        String artistDesc = s.nextLine();

        if (rmiInterface.checkArtist(artistName, artistDesc)) {
            System.out.println("Artist added.");
            menuAdministrador(username);
        } else {
            System.out.println("Error adding artist.");
            menuAdministrador(username);
        }
    }

    //insert album
    public static void insertAlbum(String username) throws RemoteException {

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
            menuAdministrador(username);
        } else {
            System.out.println("Error adding Album.");
            menuAdministrador(username);

        }
    }


    //-------- REMOVE FUNCTIONS--------//

    //remove music
    public static void removeMusic(String username) throws RemoteException{
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
            menuAdministrador(username);
        } else {
            System.out.println("Error removing music.");
            menuAdministrador(username);

        }
    }

    //remove album
    public static void removeAlbum(String username) throws RemoteException{
        System.out.println("\n\t- Remove Album -");
        Scanner s = new Scanner(System.in);

        System.out.println("Album name:");
        String albumName = s.nextLine();
        System.out.println("Artist name:");
        String artistName = s.nextLine();


        if (rmiInterface.checkRemoveAlbum(artistName,albumName)) {
            System.out.println("Album removed.");
            menuAdministrador(username);
        } else {
            System.out.println("Error removing album.");
            menuAdministrador(username);

        }
    }

    //remove artist
    public static void removeArtist(String username) throws RemoteException{
        System.out.println("\n\t- Remove Artist -");
        Scanner s = new Scanner(System.in);

        System.out.println("Artist name:");
        String artistName = s.nextLine();

        if (rmiInterface.checkRemoveArtist(artistName)) {
            System.out.println("Artist removed.");
            menuAdministrador(username);
        } else {
            System.out.println("Error removing artist.");
            menuAdministrador(username);

        }
    }


    //-------- EDIT FUNCTIONS--------//

    public static void editMusic(String username) throws RemoteException {
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
                menuAdministrador(username);
            } else {
                System.out.println("Error editing music.");
                menuAdministrador(username);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    //insert artist
    public static void editArtist(String username) throws RemoteException {
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
            menuAdministrador(username);
        } else {
            System.out.println("Error editing artist.");
            menuAdministrador(username);
        }
    }

    //insert album
    public static void editAlbum(String username) throws RemoteException {
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
            menuAdministrador(username);
        } else {
            System.out.println("Error editing album.");
            menuAdministrador(username);

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

    //-------INTERFACE----------//
    //gets client username
    public String getUsername() throws RemoteException {
        return clientUsername;
    }

    //sets client interface username
    public void setUsername(String username) throws RemoteException {
        this.clientUsername = username;
    }

    //notifies rights
    public void notifyRights(){
        System.out.println("Your rights have been changed to editor.");
    }
}

