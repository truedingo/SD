import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

public class MulticastServer extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 5000;
    private int CLIENT_PORT = 4321;
    private long SLEEP_TIME = 5000;
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
            String file_name = "test_user"+database_uid+".txt";

            while (true) {

                FileInputStream fi = new FileInputStream(file_name);
                ObjectInputStream oi = new ObjectInputStream(fi);
                FileOutputStream f = new FileOutputStream(new File(file_name));
                ObjectOutputStream o = new ObjectOutputStream(f);

                byte[] optionReceived = new byte[256];
                DatagramPacket packetOptionReceived = new DatagramPacket(optionReceived, optionReceived.length);
                socket.receive(packetOptionReceived);
                String rOption = new String(packetOptionReceived.getData(), 0, packetOptionReceived.getLength());
                System.out.println(rOption);
                if(rOption.equals("1")){

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
                }

                //read from file test
                test_user pr1 =(test_user)oi.readObject();
                System.out.println("Fetched this from file: ");
                System.out.println(pr1.toString());

                o.close();
                oi.close();

                //message2 = message2.toUpperCase();
                //byte[] send2 = message2.getBytes();
                //DatagramPacket teste = new DatagramPacket(send2, send2.length, group, CLIENT_PORT);
                //sendSocket.send(teste);

                try { sleep((long) (Math.random() * SLEEP_TIME)); } catch (InterruptedException e) { }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
