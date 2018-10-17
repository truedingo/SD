
import javax.jws.soap.SOAPBinding;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Scanner;


public class RMIServer extends UnicastRemoteObject implements RMI, Serializable {

	private static final long serialVersionUID = 1L;
	private static Configurations configurations;
	private String MULTICAST_ADDRESS = "224.0.224.0";
	private int PORT = 4444;
	private int CLIENT_PORT = 4321;
	private int MULTICAST_PORT = 5000;


	//LinkedLists
	private LinkedList<User> users;

	public RMIServer() throws RemoteException {
		super();
	}

	public String sayHello() throws RemoteException {
		System.out.println("hello");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "hello";

	}

	public void testing_connection(){
		MulticastSocket socket = null;
		MulticastSocket sendSocket = null;
		System.out.println("Test method...");
		try{
			//server stuff
			socket = new MulticastSocket(configurations.getRMIport());  // create socket without binding it (only for sending)
			sendSocket = new MulticastSocket();
			InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
			socket.joinGroup(group);

			System.out.println("Try sending something to the multicast server...");
			Scanner testScanner = new Scanner(System.in);
			String test = testScanner.nextLine();
			byte[] testBuffer = test.getBytes();
			DatagramPacket testPacket = new DatagramPacket(testBuffer, testBuffer.length, group, MULTICAST_PORT);
			sendSocket.send(testPacket);

			byte[] testReceived = new byte[256];
			DatagramPacket packetTestReceived = new DatagramPacket(testReceived, testReceived.length);
			socket.receive(packetTestReceived);
			String testString = new String(packetTestReceived.getData(), 0, packetTestReceived.getLength());
			System.out.println(testString);


		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void RMIOn() throws RemoteException {

		configurations = new Configurations(("RMI_configs.cfg"));
		System.out.println("NomeRMI: " + configurations.getRMIname());
		System.out.println("PortoRMI: " + configurations.getRMIport());
		System.out.println("HostRMI: " + configurations.getRMIhost());
		int fails=0;
		while(fails<6) {
			try {

				RMI server = (RMI) Naming.lookup("rmi://" + configurations.getRMIhost() + ":" + configurations.getRMIport() + "/" + configurations.getRMIname());
				server.sayHello();
				fails = 0;
			} catch (NotBoundException | RemoteException e) {
				System.out.print("Falha na ligacao\n");
				fails++;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}try {
			RMIServer rmiServer = new RMIServer();
			Naming.rebind("rmi://"+configurations.getRMIhost()+":"+ configurations.getRMIport()+"/"+ configurations.getRMIname(), rmiServer);
			System.out.println("\nRMI Server running on port " + configurations.getRMIport());

		}catch(ExportException e) {
			System.out.println("Entrei na exception\n");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void insertUser(User user) throws RemoteException{
		users.add(user);
	}

	public boolean verificaUserExiste(String user) throws RemoteException{
		for (User u: users){
			if(u.getUsername().equals(user))
				return false;
			else
				return true;
		}
		return true;
	}

	public boolean verifyCredencials(String username, String password) throws RemoteException{

	    for (User u: users){
	        if(u.getUsername().equals(username) && u.getPassword().equals(password)){
	            return true;
            }
            else
                return false;
        }
        return true;
    }

	// =========================================================
	public static void main(String args[]) {

		try {
			RMIOn();
		} catch (RemoteException e) {
			System.out.println("\n RemoteException : " + e.getMessage());
		}
	}
}