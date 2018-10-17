import javax.xml.crypto.Data;
import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

public class MulticastServer extends Thread {
    private String MULTICAST_ADDRESS = "224.1.224.1";
    private int PORT = 5000;
    private int CLIENT_PORT = 4321;
    private long SLEEP_TIME = 5000;
    private int RMI_PORT = 7000;
    private int database_uid = 0;

    public static void main(String[] args) {
        MulticastServer server = new MulticastServer();
        server.start();
    }

    public MulticastServer() {
        super("Server " + (long) (Math.random() * 1000));
    }

    public void run() {
        /*---*/
        System.out.println("Please enter database UID for this server: ");
        Scanner serverUIDScanner = new Scanner(System.in);
        String serverUID = serverUIDScanner.nextLine();
        int database_uid = Integer.parseInt(serverUID);
        System.out.println("Database UID for this server: "+database_uid);
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        long counter = 0;
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

                byte[] testReceived = new byte[256];
                DatagramPacket packetTestReceived = new DatagramPacket(testReceived, testReceived.length);
                socket.receive(packetTestReceived);
                String testString = new String(packetTestReceived.getData(), 0, packetTestReceived.getLength());
                System.out.println(testString);

                System.out.println("Sending answer back to RMI...");
                Scanner testScanner = new Scanner(System.in);
                String test = testScanner.nextLine();
                byte[] testBuffer = test.getBytes();
                DatagramPacket testPacket = new DatagramPacket(testBuffer, testBuffer.length, group, RMI_PORT);
                sendSocket.send(testPacket);

                /*byte[] optionReceived = new byte[256];
                DatagramPacket packetOptionReceived = new DatagramPacket(optionReceived, optionReceived.length);
                socket.receive(packetOptionReceived);
                String rOption = new String(packetOptionReceived.getData(), 0, packetOptionReceived.getLength());
                System.out.println(rOption);
                if(rOption.equals("1")){

                    FileOutputStream f = new FileOutputStream(file_name);
                    ObjectOutputStream o = new ObjectOutputStream(f);

                    //receber informações do user
                    byte[] buffer2 = new byte[256];
                    DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length);
                    socket.receive(packet2);

                    //desempacotar e meter numa classe para escrita no ficheiro de objetos
                    System.out.println("Received packet from " + packet2.getAddress().getHostAddress() + ":" + packet2.getPort() + " with message:");
                    String message2 = new String(packet2.getData(), 0, packet2.getLength());
                    System.out.println(message2);
                    String[] userInfo = message2.split("\n");
                    String name = (userInfo[0].split(":"))[1];
                    String ageS = (userInfo[1].split(":"))[1];
                    String [] age2 = ageS.split(" ");
                    int age = Integer.parseInt(age2[1]);
                    String gender = (userInfo[2].split(":"))[1];

                    test_user new_user = new test_user(name, age, gender);

                    //write to file
                    o.writeObject(new_user);
                    o.flush();
                    o.close();
                }
                else if(rOption.equals("2")){

                    FileInputStream fi = new FileInputStream(file_name);
                    ObjectInputStream oi = new ObjectInputStream(fi);
                    test_user pr1 =(test_user)oi.readObject();
                    System.out.println("Fetched this from file: ");
                    System.out.println(pr1.toString());
                    oi.close();
                }*/


                //read from file test

                //message2 = message2.toUpperCase();
                //byte[] send2 = message2.getBytes();
                //DatagramPacket teste = new DatagramPacket(send2, send2.length, group, CLIENT_PORT);
                //sendSocket.send(teste);

                try { sleep((long) (Math.random() * SLEEP_TIME)); } catch (InterruptedException e) { }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
