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

                try { sleep((long) (Math.random() * SLEEP_TIME)); } catch (InterruptedException e) { }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }
    }

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

    public boolean login(String username, String password){
        for(User u: usersArrayList){
            if(u.getUsername().equals(username) && u.getPassword().equals(password)){
                u.setStatus(true);
                return true;
            }
        }
        return false;
    }

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

    public void setPrivilege(String username){
        for(User u: usersArrayList){
            if(u.getUsername().equals(username)){
                u.setPrivilege(true);
            }
        }
    }
}
