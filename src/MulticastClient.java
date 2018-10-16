import javax.xml.crypto.Data;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Scanner;

/**
 * The MulticastClient class joins a multicast group and loops receiving
 * messages from that group. The client also runs a MulticastUser thread that
 * loops reading a string from the keyboard and multicasting it to the group.
 * <p>
 * The example IPv4 address chosen may require you to use a VM option to
 * prefer IPv4 (if your operating system uses IPv6 sockets by default).
 * <p>
 * Usage: java -Djava.net.preferIPv4Stack=true MulticastClient
 *
 * @author Raul Barbosa
 * @version 1.0
 */
public class MulticastClient extends Thread {

    public static void main(String[] args) {
        MulticastClient client = new MulticastClient();
        client.start();
        MulticastUser user = new MulticastUser();
        user.start();
    }
}

class MulticastUser extends Thread {
    private String MULTICAST_ADDRESS = "224.1.224.1";
    private int PORT = 4321;
    private int SERVER_PORT = 5000;

    public MulticastUser() {
        super("User " + (long) (Math.random() * 1000));
    }

    public void run() {
        MulticastSocket socket = null;
        MulticastSocket receiveSocket = null;
        System.out.println(this.getName() + " ready...");
        try {
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            receiveSocket = new MulticastSocket(PORT);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            receiveSocket.joinGroup(group);
            while (true) {
                System.out.println("Please enter option:");
                System.out.println("1. Register new person");
                System.out.println("2. Check available readable file");
                Scanner checker = new Scanner(System.in);
                String reading = checker.nextLine();
                if(reading.equals("1")){

                    String serverOption = "1";
                    byte[] serverOptionBuffer = serverOption.getBytes();
                    DatagramPacket optionPacket = new DatagramPacket(serverOptionBuffer, serverOptionBuffer.length, group, SERVER_PORT);
                    socket.send(optionPacket);
                    System.out.println("Sending option to server...");

                    System.out.println("Please enter your name: ");
                    Scanner nameScanner = new Scanner(System.in);
                    String name = nameScanner.nextLine();

                    System.out.println("Please enter your age: ");
                    Scanner ageScanner = new Scanner(System.in);
                    String age = ageScanner.nextLine();
                    int intAge = Integer.parseInt(age);

                    System.out.println("Please enter your gender: ");
                    Scanner genderScanner = new Scanner(System.in);
                    String gender = genderScanner.nextLine();

                    test_user newuser = new test_user(name, intAge, gender);
                    System.out.println("New user created!");
                    String user = newuser.toString();

                    byte[] bufferuser = user.getBytes();
                    DatagramPacket userPacket = new DatagramPacket(bufferuser, bufferuser.length, group, SERVER_PORT);
                    socket.send(userPacket);
                    System.out.println("User information sent to server!");
                }

                /*-----*/

                //byte[] teste = new byte[256];
                //DatagramPacket teste2 = new DatagramPacket(teste, teste.length);
                //receiveSocket.receive(teste2);

                //System.out.println("Received packet from " + teste2.getAddress().getHostAddress() + ":" + teste2.getPort() + " with message:");
                //String message2 = new String(teste2.getData(), 0, teste2.getLength());
                //System.out.println(message2);


            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
