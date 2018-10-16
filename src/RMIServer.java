import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.*;
import java.net.*;



public class RMIServer extends UnicastRemoteObject implements RMI, Serializable {

	private static final long serialVersionUID = 1L;
	private static Configurations configurations;
	private String MULTICAST_ADDRESS = "224.0.224.0";
	private int PORT = 4444;
	private int CLIENT_PORT = 4321;
	private int MULTICAST_PORT = 5000;

	public RMIServer() throws RemoteException {
		super();
	}

	public void sayHello() throws RemoteException {
		System.out.println("ping");

	}

	public boolean metodoTeste(){

		try{
			MulticastSocket socket = null;
			MulticastSocket sendSocket = null;
			InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
			socket.joinGroup(group);

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

	// =========================================================
	public static void main(String args[]) {

		try {
			RMIOn();
		} catch (RemoteException e) {
			System.out.println("\n RemoteException : " + e.getMessage());
		}
	}
}