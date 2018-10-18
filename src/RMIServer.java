
import java.io.IOException;
import java.io.Serializable;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;


public class RMIServer extends UnicastRemoteObject implements RMI, Serializable {

	private static final long serialVersionUID = 1L;
	private static Configurations configurations;
    private String MULTICAST_ADDRESS = "224.1.224.1";
	private static int PORT = 7000;
	private static int CLIENT_PORT = 4321;
	private static int MULTICAST_PORT = 5000;

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

	public boolean checkRegister(String username, String password) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String stringRegister = "type|register;username|" + username + ";password|" + password;
            System.out.println(stringRegister);
            byte[] bufferRegister = stringRegister.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferRegister, bufferRegister.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: "+stringRegister);

            byte[] bufferReceiveRegister = new byte[256];
            DatagramPacket receivePacketRegister = new DatagramPacket(bufferReceiveRegister, bufferReceiveRegister.length);
            socket.receive(receivePacketRegister);
            String receiveRegister = new String(receivePacketRegister.getData(), 0, receivePacketRegister.getLength());
            System.out.println("Received from Multicast: "+receiveRegister);
            if(receiveRegister.equals("type|status;logged|off;msg|ErrorWithUsername")){
                return false;
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }
        return true;
    }

    public boolean checkLogin(String username, String password) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String stringLogin = "type|login;username|" + username + ";password|" + password;
            System.out.println(stringLogin);
            byte[] bufferLogin = stringLogin.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferLogin, bufferLogin.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: "+stringLogin);

            byte[] bufferReceiveLogin = new byte[256];
            DatagramPacket receivePacketLogin = new DatagramPacket(bufferReceiveLogin, bufferReceiveLogin.length);
            socket.receive(receivePacketLogin);
            String receiveLogin = new String(receivePacketLogin.getData(), 0, receivePacketLogin.getLength());
            System.out.println("Received from Multicast: "+receiveLogin);
            if(receiveLogin.equals("type|status;logged|on;msg|ErrorWithLogin")){
                return false;
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            socket.close();
            sendSocket.close();
        }
        return true;
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

		}catch(ExportException | MalformedURLException e) {
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