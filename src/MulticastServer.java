import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;


public class MulticastServer extends Thread {
    private String MULTICAST_ADDRESS = "224.1.224.1";
    private int PORT = 5000;
    private int CLIENT_PORT = 4321;
    private long SLEEP_TIME = 5000;
    private int RMI_PORT = 7000;
    private int database_uid = 0;

    private CopyOnWriteArrayList<User> usersArrayList;
    private CopyOnWriteArrayList<Artist> artistsArrayList;

    public static void main(String[] args) {
        MulticastServer server = new MulticastServer();
        server.start();
    }

    public MulticastServer() {
        super("Server " + (long) (Math.random() * 1000));
    }

    public void run() {
        /*---*/
        usersArrayList = new CopyOnWriteArrayList<>();
        artistsArrayList = new CopyOnWriteArrayList<>();
        User admin = new User("admin", "admin");
        admin.setPrivilege(true);
        usersArrayList.add(admin);
        System.out.println("Please enter database UID for this server: ");
        Scanner serverUIDScanner = new Scanner(System.in);
        String serverUID = serverUIDScanner.nextLine();
        int database_uid = Integer.parseInt(serverUID);
        System.out.println("Database UID for this server: "+database_uid);
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        System.out.println(this.getName() + " running...");
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            //files stuff
            String file_name = "/Users/dingo/Desktop/SD/DropMusicMerged/test_user"+database_uid+".txt";

            while (true) {
                byte[] receiveBuffer = new byte[256];
                DatagramPacket  receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                String receiveString = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received from RMI: "+receiveString);

                if(receiveString.contains("type|register")){
                    String [] splitString = receiveString.split(";");
                    String [] getUsernameString = splitString[1].split("\\|");
                    String [] getPasswordString = splitString[2].split("\\|");
                    String getUsername = getUsernameString[1];
                    String getPassword = getPasswordString[1];
                    boolean flag;
                    System.out.println("Trying to register user with Username:"+getUsername+" Password:"+getPassword);
                    flag = register(getUsername, getPassword);
                    System.out.println(flag);
                    if(flag){
                        String sendRegister = "type|status;logged|on;msg|UserRegistered";
                        byte[] sendBufferRegister = sendRegister.getBytes();
                        DatagramPacket sendRegisterPacket = new DatagramPacket(sendBufferRegister, sendBufferRegister.length, group, RMI_PORT);
                        sendSocket.send(sendRegisterPacket);
                        System.out.println();
                        System.out.println("Sent to RMI: "+sendRegister);
                    }
                    else{
                        System.out.println("Username already in use");
                        String sendError = "type|status;logged|off;msg|ErrorWithUsername";
                        byte[] sendBufferError = sendError.getBytes();
                        DatagramPacket sendErrorPacket = new DatagramPacket(sendBufferError, sendBufferError.length, group, RMI_PORT);
                        sendSocket.send(sendErrorPacket);
                        System.out.println("Sent to RMI: " + sendError);
                    }
                }
                else if(receiveString.contains("type|login")){
                    String [] splitString = receiveString.split(";");
                    String [] getUsernameString = splitString[1].split("\\|");
                    String [] getPasswordString = splitString[2].split("\\|");
                    String getUsername = getUsernameString[1];
                    String getPassword = getPasswordString[1];
                    boolean flag;
                    boolean check;
                    System.out.println("Trying to login user with Username:"+getUsername+" Password:"+getPassword);

                    flag = login(getUsername, getPassword);
                    if(flag){
                        check = checkPrivilege(getUsername);
                        if(check){
                            String sendLogin = "type|status;logged|on;msg|WelcomeToDropMusic|privilege;editor|";
                            byte[] sendBufferLogin = sendLogin.getBytes();
                            DatagramPacket sendLoginPacket = new DatagramPacket(sendBufferLogin, sendBufferLogin.length, group, RMI_PORT);
                            sendSocket.send(sendLoginPacket);
                            System.out.println("Sent to RMI: "+sendLogin);
                        }
                        else{
                            String sendLogin = "type|status;logged|on;msg|WelcomeToDropMusic|privilege;user|";
                            byte[] sendBufferLogin = sendLogin.getBytes();
                            DatagramPacket sendLoginPacket = new DatagramPacket(sendBufferLogin, sendBufferLogin.length, group, RMI_PORT);
                            sendSocket.send(sendLoginPacket);
                            System.out.println("Sent to RMI: "+sendLogin);
                        }
                    }
                    else{
                        String sendLogin = "type|status;logged|on;msg|ErrorWithLogin";
                        byte[] sendBufferLogin = sendLogin.getBytes();
                        DatagramPacket sendLoginPacket = new DatagramPacket(sendBufferLogin, sendBufferLogin.length, group, RMI_PORT);
                        sendSocket.send(sendLoginPacket);
                        System.out.println("Sent to RMI: "+sendLogin);
                    }

                }
                else if(receiveString.contains("type|check;rights")){
                    String [] splitString = receiveString.split(";");
                    String getUsername = splitString[2];
                    System.out.println("Trying to change rights of user with Username:"+getUsername);

                    boolean check = checkUserRights(getUsername);
                    if(check){
                        setPrivilege(getUsername);
                        System.out.println("Changed rights of "+getUsername+" to editor.");
                        String sendCheck = "type|check;rights|changed";
                        byte[] sendBufferCheck = sendCheck.getBytes();
                        DatagramPacket sendCheckPacket = new DatagramPacket(sendBufferCheck, sendBufferCheck.length, group, RMI_PORT);
                        sendSocket.send(sendCheckPacket);
                        System.out.println("Sent to RMI: "+sendCheck);
                    }
                    else{
                        String sendCheck = "type|check;rights|error";
                        byte[] sendBufferCheck = sendCheck.getBytes();
                        DatagramPacket sendCheckPacket = new DatagramPacket(sendBufferCheck, sendBufferCheck.length, group, RMI_PORT);
                        sendSocket.send(sendCheckPacket);
                        System.out.println("Sent to RMI: "+sendCheck);
                    }

                }
                else if(receiveString.contains("type|idle;rights|")){
                    String [] splitString = receiveString.split(";");
                    String [] splitString2 = splitString[1].split("\\|");
                    String getUsername = splitString2[1];
                    System.out.println("IdleCheck: "+getUsername);
                    boolean flag = checkPrivilege(getUsername);
                    if(flag){
                        String sendIdle = "type|idle;rights|editor";
                        byte[] sendBufferIdle = sendIdle.getBytes();
                        DatagramPacket sendCheckPacket = new DatagramPacket(sendBufferIdle, sendBufferIdle.length, group, RMI_PORT);
                        sendSocket.send(sendCheckPacket);
                        System.out.println("Sent to RMI: "+sendIdle);
                    }
                    else{
                        String sendIdle = "type|idle;rights|user";
                        byte[] sendIdleCheck = sendIdle.getBytes();
                        DatagramPacket sendIdlePacket = new DatagramPacket(sendIdleCheck, sendIdleCheck.length, group, RMI_PORT);
                        sendSocket.send(sendIdlePacket);
                        System.out.println("Sent to RMI: "+sendIdle);
                    }

                }
                else if(receiveString.contains("type|insert_album")){
                    String [] splitString = receiveString.split(";");

                    String getAlbumName = (splitString[1].split("\\|"))[1];

                    String getDescription = (splitString[2].split("\\|"))[1];

                    String getMusicGenre = (splitString[3].split("\\|"))[1];

                    String getDate = (splitString[4].split("\\|"))[1];

                    String getArtistName = (splitString[5].split("\\|"))[1];

                    boolean flag;
                    flag = checkArtistExist(getArtistName);
                    System.out.println(flag);
                    if(!flag){
                        addAlbum(getAlbumName, getDescription, getMusicGenre, getDate, getArtistName);
                        String sendAlbum= "type|insert_album;successful";

                        byte[] sendBufferAlbum = sendAlbum.getBytes();
                        DatagramPacket artistPacket = new DatagramPacket(sendBufferAlbum, sendBufferAlbum.length, group, RMI_PORT);
                        sendSocket.send(artistPacket);
                        System.out.println("Sent to RMI: "+ sendAlbum);
                    }
                    else{
                        String sendArtist= "type|insert_album;error in insert album";

                        byte[] sendBufferAlbum = sendArtist.getBytes();
                        DatagramPacket artistPacket = new DatagramPacket(sendBufferAlbum, sendBufferAlbum.length, group, RMI_PORT);
                        sendSocket.send(artistPacket);
                        System.out.println("Sent to RMI: "+ sendArtist);
                    }


                }
                else if(receiveString.contains("type|insert_artist")){
                    String [] splitString = receiveString.split(";");
                    String artistName = splitString[1];
                    String [] splitString2 = splitString[2].split("\\|");
                    String artistDescription = splitString2[1];

                    boolean flag;
                    boolean check;
                    flag = checkArtistExist(artistName);
                    System.out.println(flag);
                    if(flag){
                        //Adiciona artista a lista
                        addArtist(artistName,artistDescription);
                        String sendArtist= "type|insert_artist;successful";

                        byte[] sendBufferArtist = sendArtist.getBytes();
                        DatagramPacket artistPacket = new DatagramPacket(sendBufferArtist, sendBufferArtist.length, group, RMI_PORT);
                        sendSocket.send(artistPacket);
                        System.out.println("Sent to RMI: "+ sendArtist);
                    }
                    else{
                        String sendArtist= "type|insert_artist;error in insert artist";

                        byte[] sendBufferArtist = sendArtist.getBytes();
                        DatagramPacket artistPacket = new DatagramPacket(sendBufferArtist, sendBufferArtist.length, group, RMI_PORT);
                        sendSocket.send(artistPacket);
                        System.out.println("Sent to RMI: "+ sendArtist);
                    }


                }
                else if(receiveString.contains("type|insert_music")){
                    String [] splitString = receiveString.split(";");

                    String getMusicName = (splitString[1].split("\\|"))[1];

                    String getMusicGenre = (splitString[2].split("\\|"))[1];

                    String getDuration = (splitString[3].split("\\|"))[1];

                    String getArtistName = (splitString[4].split("\\|"))[1];

                    String getAlbumName = (splitString[5].split("\\|"))[1];

                    String getLyrics = (splitString[6].split("\\|"))[1];

                    String getDate = (splitString[7].split("\\|"))[1];

                    boolean flag;
                    boolean check;
                    boolean check2;
                    //Se o album existir
                    //se o artista existir

                    flag = checkArtistExist(getArtistName);
                    check = checkAlbumExist(getAlbumName, getArtistName);
                    check2 = checkMusicRepetition(getArtistName,getAlbumName,getMusicName);

                    System.out.println(flag);
                    if(!flag && !check && check2){
                        //Adiciona musica a lista
                        addSong(getMusicName, getMusicGenre, getDuration,getDate, getLyrics, getAlbumName, getArtistName);
                        String sendArtist= "type|insert_music;successful";

                        byte[] sendBufferArtist = sendArtist.getBytes();
                        DatagramPacket artistPacket = new DatagramPacket(sendBufferArtist, sendBufferArtist.length, group, RMI_PORT);
                        sendSocket.send(artistPacket);
                        System.out.println("Sent to RMI: "+ sendArtist);
                    }
                    else{
                        String sendArtist= "type|insert_music;error in insert music";

                        byte[] sendBufferArtist = sendArtist.getBytes();
                        DatagramPacket artistPacket = new DatagramPacket(sendBufferArtist, sendBufferArtist.length, group, RMI_PORT);
                        sendSocket.send(artistPacket);
                        System.out.println("Sent to RMI: "+ sendArtist);
                    }
                }
                else if(receiveString.contains("type|remove_music")){
                    String [] splitString = receiveString.split(";");
                    //nome musica
                    String [] splitString1 =splitString[1].split("\\|");
                    String musicName = splitString1[1];
                    //artista
                    String [] splitString2 = splitString[2].split("\\|");
                    String artistName = splitString2[1];
                    //album
                    String [] splitString3 = splitString[3].split("\\|");
                    String albumName = splitString3[1];

                    boolean flag;
                    boolean check;
                    boolean check2;
                    //Se o album existir
                    //se o artista existir

                    flag = checkArtistExist(artistName);
                    check = checkAlbumExist(albumName, artistName);

                    System.out.println(flag);
                    if(!flag && !check){
                        removeMusic(musicName,artistName,albumName);
                        String sendArtist= "type|remove_music;successful";
                        byte[] sendBufferRemoveMusic = sendArtist.getBytes();
                        DatagramPacket removeMusicPacket = new DatagramPacket(sendBufferRemoveMusic, sendBufferRemoveMusic.length, group, RMI_PORT);
                        sendSocket.send(removeMusicPacket);
                        System.out.println("Sent to RMI: "+ sendArtist);
                    }
                    else{
                        String sendArtist= "type|remove_music;error in remove music";

                        byte[] sendBufferArtist = sendArtist.getBytes();
                        DatagramPacket artistPacket = new DatagramPacket(sendBufferArtist, sendBufferArtist.length, group, RMI_PORT);
                        sendSocket.send(artistPacket);
                        System.out.println("Sent to RMI: "+ sendArtist);
                    }


                }
                else if(receiveString.contains("type|remove_album")) {
                    String[] splitString = receiveString.split(";");
                    //album
                    String [] splitString1 =splitString[1].split("\\|");
                    String albumName = splitString1[1];

                    //artista
                    String [] splitString2 = splitString[2].split("\\|");
                    String artistName = splitString2[1];

                    boolean flag;
                    boolean check;
                    //Se o album existir
                    //se o artista existir

                    flag = checkArtistExist(artistName);
                    check = checkAlbumExist(albumName, artistName);

                    System.out.println(flag);
                    if (!flag && !check) {
                        removeAlbum(artistName,albumName);

                        String sendArtist = "type|remove_album;successful";
                        byte[] sendBufferRemoveMusic = sendArtist.getBytes();
                        DatagramPacket removeMusicPacket = new DatagramPacket(sendBufferRemoveMusic, sendBufferRemoveMusic.length, group, RMI_PORT);
                        sendSocket.send(removeMusicPacket);
                        System.out.println("Sent to RMI: " + sendArtist);
                    } else {
                        String sendArtist = "type|remove_album;error in remove album";

                        byte[] sendBufferArtist = sendArtist.getBytes();
                        DatagramPacket artistPacket = new DatagramPacket(sendBufferArtist, sendBufferArtist.length, group, RMI_PORT);
                        sendSocket.send(artistPacket);
                        System.out.println("Sent to RMI: " + sendArtist);
                    }
                }
                else if(receiveString.contains("type|remove_artist")) {
                    String[] splitString = receiveString.split(";");
                    //album
                    String [] splitString1 =splitString[1].split("\\|");
                    String artistName = splitString1[1];

                    boolean flag;

                    flag = checkArtistExist(artistName);

                    System.out.println(flag);
                    if (!flag) {
                        removeArtist(artistName);
                        String sendArtist = "type|remove_artist;successful";
                        byte[] sendBufferRemoveMusic = sendArtist.getBytes();
                        DatagramPacket removeMusicPacket = new DatagramPacket(sendBufferRemoveMusic, sendBufferRemoveMusic.length, group, RMI_PORT);
                        sendSocket.send(removeMusicPacket);
                        System.out.println("Sent to RMI: " + sendArtist);
                    } else {
                        String sendArtist = "type|remove_artist;error in remove album";

                        byte[] sendBufferArtist = sendArtist.getBytes();
                        DatagramPacket artistPacket = new DatagramPacket(sendBufferArtist, sendBufferArtist.length, group, RMI_PORT);
                        sendSocket.send(artistPacket);
                        System.out.println("Sent to RMI: " + sendArtist);
                    }
                }
                else if(receiveString.contains("type|edit_artist")){

                    String [] splitString = receiveString.split(";");

                    System.out.println(splitString[0]);
                    System.out.println(splitString[1]);
                    System.out.println(splitString[2]);
                    System.out.println(splitString[3]);

                    String[] splitString2 = splitString[1].split("\\|");
                    String getOldArtistName = splitString2[1];

                    String[] splitString3 = splitString[2].split("\\|");
                    String getNewArtistName = splitString3[1];

                    String[] splitString4 = splitString[3].split("\\|");
                    String getDescription = splitString4[0];

                    boolean flag = editArtist(getOldArtistName, getNewArtistName, getDescription);

                    System.out.println(flag);
                    if (flag) {
                        String sendEditArtist = "type|edit_artist;successful";
                        byte[] sendBufferEditArtist = sendEditArtist.getBytes();
                        DatagramPacket sendEditArtistPacket = new DatagramPacket(sendBufferEditArtist, sendBufferEditArtist.length, group, RMI_PORT);
                        sendSocket.send(sendEditArtistPacket);
                        System.out.println("Sent to RMI: " + sendEditArtist);
                    } else {
                        String sendEditArtist = "type|edit_artist;error in edit artist";
                        byte[] sendBufferEditArtist = sendEditArtist.getBytes();
                        DatagramPacket sendEditArtistPacket = new DatagramPacket(sendBufferEditArtist, sendBufferEditArtist.length, group, RMI_PORT);
                        sendSocket.send(sendEditArtistPacket);
                        System.out.println("Sent to RMI: " + sendEditArtist);
                    }

                }
                else if(receiveString.contains("type|edit_album")){

                    String [] splitString = receiveString.split(";");

                    String getArtistName = (splitString[1].split("\\|"))[1];

                    String getOldAlbumName = (splitString[2].split("\\|"))[1];

                    String getNewAlbumName = (splitString[3].split("\\|"))[1];

                    String getDescription = (splitString[4].split("\\|"))[1];

                    String getMusicGenre = (splitString[5].split("\\|"))[1];

                    String getDate = (splitString[6].split("\\|"))[1];

                    boolean flag = editAlbum(getArtistName, getOldAlbumName, getNewAlbumName, getDescription, getMusicGenre, getDate);

                    System.out.println(flag);
                    if (flag) {
                        String sendEditAlbum = "type|edit_album;successful";
                        byte[] sendBufferEditAlbum = sendEditAlbum.getBytes();
                        DatagramPacket sendEditAlbumPacket = new DatagramPacket(sendBufferEditAlbum, sendBufferEditAlbum.length, group, RMI_PORT);
                        sendSocket.send(sendEditAlbumPacket);
                        System.out.println("Sent to RMI: " + sendEditAlbum);
                    } else {
                        String sendEditAlbum = "type|edit_album;error in edit album";
                        byte[] sendBufferEditAlbum = sendEditAlbum.getBytes();
                        DatagramPacket sendEditAlbumPacket = new DatagramPacket(sendBufferEditAlbum, sendBufferEditAlbum.length, group, RMI_PORT);
                        sendSocket.send(sendEditAlbumPacket);
                        System.out.println("Sent to RMI: " + sendEditAlbum);
                    }

                }
                else if(receiveString.contains("type|edit_music")){

                    String[] splitString = receiveString.split(";");

                    String getArtistName = (splitString[1].split("\\|"))[1];

                    String getAlbumName = (splitString[2].split("\\|"))[1];

                    String getOldMusicName = (splitString[3].split("\\|"))[1];

                    String getNewMusicName = (splitString[4].split("\\|"))[1];

                    String getGenre = (splitString[5].split("\\|"))[1];

                    String getDuration = (splitString[6].split("\\|"))[1];

                    String getDate = (splitString[7].split("\\|"))[1];

                    String getLyrics = (splitString[8].split("\\|"))[1];

                    boolean flag = editMusic(getArtistName, getAlbumName, getOldMusicName, getNewMusicName, getGenre, getDuration, getDate, getLyrics);

                    System.out.println(flag);

                    if (flag) {
                        String sendEditMusic = "type|edit_music;successful";
                        byte[] sendBufferEditMusic = sendEditMusic.getBytes();
                        DatagramPacket sendEditMusicPacket = new DatagramPacket(sendBufferEditMusic, sendBufferEditMusic.length, group, RMI_PORT);
                        sendSocket.send(sendEditMusicPacket);
                        System.out.println("Sent to RMI: " + sendEditMusic);
                    } else {
                        String sendEditMusic = "type|edit_music;error in edit music";
                        byte[] sendBufferEditMusic = sendEditMusic.getBytes();
                        DatagramPacket sendEditMusicPacket = new DatagramPacket(sendBufferEditMusic, sendBufferEditMusic.length, group, RMI_PORT);
                        sendSocket.send(sendEditMusicPacket);
                        System.out.println("Sent to RMI: " + sendEditMusic);
                    }

                }
                else if(receiveString.contains("type|write_critic")){

                    String [] splitString = receiveString.split(";");
                    //user
                    String [] splitString1 =splitString[1].split("\\|");
                    String username = splitString1[1];
                    //artista
                    String [] splitString2 =splitString[2].split("\\|");
                    String artistName = splitString2[1];
                    //album
                    String [] splitString3 =splitString[3].split("\\|");
                    String albumName = splitString3[1];
                    //rate
                    String [] splitString4 =splitString[4].split("\\|");
                    String rate = splitString4[1];
                    //Desc
                    String [] splitString5=splitString[5].split("\\|");
                    String descricao = splitString5[1];

                    int intRate = Integer.parseInt(rate);

                    boolean flag = checkAlbumExist(albumName,artistName);
                    boolean flag2 = checkArtistExist(artistName);

                    System.out.println(flag);
                    if (!flag && !flag2) {
                        //add critic
                        addCritic(username,artistName,albumName,intRate,descricao);
                        String sendWriteCritic = "type|write_critic;successful";
                        byte[] sendWriteCriticBuffer = sendWriteCritic.getBytes();
                        DatagramPacket sendWriteCriticPacket = new DatagramPacket(sendWriteCriticBuffer, sendWriteCriticBuffer.length, group, RMI_PORT);
                        sendSocket.send(sendWriteCriticPacket);
                        System.out.println("Sent to RMI: " + sendWriteCritic);
                    } else {
                        String sendWriteCritic = "type|write_critic;error in write critic";
                        byte[] sendWriteCriticBuffer = sendWriteCritic.getBytes();
                        DatagramPacket sendWriteCriticPacket = new DatagramPacket(sendWriteCriticBuffer, sendWriteCriticBuffer.length, group, RMI_PORT);
                        sendSocket.send(sendWriteCriticPacket);
                        System.out.println("Sent to RMI: " + sendWriteCritic);
                    }

                }



                try { sleep((long) (Math.random() * SLEEP_TIME)); } catch (InterruptedException e) { }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }
    }


    //-------- LOGIN/REGISTER--------//

    //register
    public boolean register(String username, String password){
        for(User u: usersArrayList){
            if(u.getUsername().equals(username)){
                return false;
            }
        }
        User user = new User(username, password);
        user.setPrivilege(false);
        usersArrayList.add(user);
        return true;
    }

    //login
    public boolean login(String username, String password){
        for(User u: usersArrayList){
            if(u.getUsername().equals(username) && u.getPassword().equals(password)){
                u.setStatus(true);
                return true;
            }
        }
        return false;
    }


    //-------- CHECK--------//

    //check privilege (MEXER NISTO EVENTUALMENTE)
    public boolean checkPrivilege(String username){
        for(User u: usersArrayList){
            if(u.getUsername().equals(username)){
                if(u.isPrivilege()){
                    return true;
                }
            }
        }
        return false;
    }

    //check user rights
    public boolean checkUserRights(String username){
        for(User u: usersArrayList){
            if(u.getUsername().equals(username)){
                if(u.isPrivilege())
                    return false;
                else
                    return true;

            }
        }
        return false;
    }

    //check artist - funcao que verifica se o artista que vamos inserir ja existe - se ja existir retorna false, se nao retorna true
    public boolean checkArtistExist(String name) {
        if (!artistsArrayList.isEmpty()) {
            for (Artist a : artistsArrayList) {
                if (a.getArtistName().equals(name)) {
                    return false;
                }
            }
        }
        return true;
    }

    //check album - Se ja existir um album com esse nome retorna falso
    public boolean checkAlbumExist(String albumName, String artistName) {
        for(Artist a: artistsArrayList){
            if(a.getArtistName().equals(artistName)){
                for(Album alb: a.getAlbums()){
                    if(alb.getAlbumName().equals(albumName)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //check music - se ja existir uma musica desse artista com esse nome falso
    public boolean checkMusicRepetition(String artistName,String albumName,String musicName){
        for (Artist a: artistsArrayList){
            if(a.getArtistName().equals(artistName)){
                for(Album alb : a.getAlbums()){
                    if(alb.getAlbumName().equals(albumName)){
                        for(Song s : alb.getSongs()){
                            if(s.getSongName().equals(musicName)){
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }


    //-------- ADD --------//

    //add artist
    public void addArtist(String name, String description){
        Artist a;
        a = new Artist(name, description);
        artistsArrayList.add(a);

        //DEBUG
        System.out.println(a);
    }

    //add song
    public void addSong(String name, String genre, String duration, String udate, String lyrics, String albumName, String artistName){

        for(Artist a: artistsArrayList){
            if(a.getArtistName().equals(artistName)){
                for(Album alb : a.getAlbums()){
                    if(alb.getAlbumName().equals(albumName)){
                        Song s = new Song(name, genre, duration, udate, lyrics);
                        alb.addSongs(s);
                        //DEBUG
                        System.out.println(s);
                    }
                }
            }
        }
    }

    //add album
    public void addAlbum(String albumName, String desc, String musicGenre, String date, String artistName){
        for(Artist a: artistsArrayList){
            if(a.getArtistName().equals(artistName)){
                Album album = new Album(albumName, desc, date, musicGenre);
                a.addAlbums(album);
                //DEBUG
                System.out.println(album);
            }
        }
    }

    //add critic
    public void addCritic(String username,String artistName, String albumName, int rate, String critic){
        for(Artist a: artistsArrayList){
            if(a.getArtistName().equals(artistName)){
                for(Album alb : a.getAlbums()){
                    if(alb.getAlbumName().equals(albumName)){
                        Critic c = new Critic(rate, username, critic);
                        alb.addCritics(c);
                        alb.addAvgRate(rate);

                        //DEBUG
                        System.out.println(c);
                    }
                }
            }
        }
    }

    //-------- REMOVE --------//

    //remove music
    public void removeMusic(String musicName, String artistName, String albumName) {
        Album getAlbum = null;
        Song removeSong = null;
        if (!artistsArrayList.isEmpty()) {
            for (Artist a : artistsArrayList) {
                if (a.getArtistName().equals(artistName)) {
                    for (Album alb : a.getAlbums()) {
                        if (alb.getAlbumName().equals(albumName)) {
                            getAlbum = alb;
                            if (!alb.getSongs().isEmpty()) {
                                for (Song s : alb.getSongs()) {
                                    if (s.getSongName().equals(musicName)) {
                                        removeSong = s;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        getAlbum.removeSongs(removeSong);
    }

    //remove album
    public void removeAlbum(String artistName,String albumName) {
        Artist getArtist = null;
        Album removeAlbum = null;
        if (!artistsArrayList.isEmpty()) {
            for (Artist a : artistsArrayList) {
                if (a.getArtistName().equals(artistName)) {
                    getArtist = a;
                    for (Album alb : a.getAlbums()) {
                        if (alb.getAlbumName().equals(albumName)) {
                            removeAlbum = alb;
                        }
                    }
                }
            }
        }
        getArtist.removeAlbum(removeAlbum);
    }

    //remove artist
    public void removeArtist(String artistName) {
        Artist removeArtist = null;
        if (!artistsArrayList.isEmpty()) {
            for (Artist a : artistsArrayList) {
                if (a.getArtistName().equals(artistName)) {
                    removeArtist = a;
                }
            }
        }
        artistsArrayList.remove(removeArtist);
    }


    //-------- EDIT --------//

    //edit artist
    public boolean editArtist(String oldArtistName, String newArtistName, String description) {
        if (!artistsArrayList.isEmpty()) {
            for (Artist a : artistsArrayList) {
                if (a.getArtistName().equals(oldArtistName)) {
                    a.setArtistName(newArtistName);
                    a.setDescArtist(description);

                    //DEBUG
                    System.out.println(a);
                    return true;
                }
            }
        }
        return false;
    }

    //edit album
    public boolean editAlbum(String artistName, String OldAlbumName, String newAlbumName, String description, String musicGenre, String udate){
        if (!artistsArrayList.isEmpty()) {
            for (Artist a : artistsArrayList) {
                if (a.getArtistName().equals(artistName)) {
                    for (Album alb : a.getAlbums()) {
                        if (alb.getAlbumName().equals(OldAlbumName)) {
                            alb.setAlbumName(newAlbumName);
                            alb.setDescription(description);
                            alb.setMusicalGenre(musicGenre);
                            alb.setReleaseDate(udate);

                            //DEBUG
                            System.out.println(alb);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //edit music
    public boolean editMusic(String artistName, String albumName, String oldMusicName, String newMusicName, String musicGenre, String duration, String date, String lyrics){
        if (!artistsArrayList.isEmpty()) {
            for (Artist a : artistsArrayList) {
                if (a.getArtistName().equals(artistName)) {
                    for (Album alb : a.getAlbums()) {
                        if (alb.getAlbumName().equals(albumName)) {
                            if (!alb.getSongs().isEmpty()) {
                                for (Song s : alb.getSongs()) {
                                    if (s.getSongName().equals(oldMusicName)) {
                                        s.setSongName(newMusicName);
                                        s.setSongGenre(musicGenre);
                                        s.setDuration(duration);
                                        s.setReleaseDate(date);
                                        s.setLyrics(lyrics);

                                        //DEBUG
                                        System.out.println(s);
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }




    //-------- OTHER --------//

    //set privilege
    public void setPrivilege(String username){
        for(User u: usersArrayList){
            if(u.getUsername().equals(username)){
                u.setPrivilege(true);
            }
        }
    }

    //avg rate calc
    public float calAvgRate(String artistName, String albumName){
        int sum = 0;
        float totalRates = 0;
        for(Artist a: artistsArrayList){
            if(a.getArtistName().equals(artistName)){
                for(Album alb : a.getAlbums()){
                    if(alb.getAlbumName().equals(albumName)){
                        for (int i = 0; i < alb.getMediaRating().size() ; i++) {
                            sum+= alb.getMediaRating().get(i);
                            totalRates++;

                        }
                    }
                }
            }
        }
        return sum/totalRates;
    }
}
