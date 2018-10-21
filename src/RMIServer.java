
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

	public synchronized String sayHello() throws RemoteException {
		System.out.println("hello");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "hello";
	}

	public synchronized boolean checkRegister(String username, String password) {
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

    public synchronized boolean checkUserRights(String username){
	    MulticastSocket socket = null;
	    MulticastSocket sendSocket = null;
	    try{
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String checkRight = "type|check;rights|username;"+username;
            System.out.println(checkRight);
            byte[] bufferCheckRight = checkRight.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferCheckRight, bufferCheckRight.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: "+checkRight);

            byte[] bufferReceiveCheck = new byte[256];
            DatagramPacket receivePacketCheck = new DatagramPacket(bufferReceiveCheck, bufferReceiveCheck.length);
            socket.receive(receivePacketCheck);
            String receiveCheck = new String(receivePacketCheck.getData(), 0, receivePacketCheck.getLength());
            System.out.println("Received from Multicast: "+receiveCheck);
            if(receiveCheck.equals("type|check;rights|error")){
                return false;
            }



        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public synchronized boolean idleUserRights(String username) {

        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String idleCheck = "type|idle;rights|"+username;
            System.out.println(idleCheck);
            byte[] bufferIdleRight = idleCheck.getBytes();
            DatagramPacket idlePacket = new DatagramPacket(bufferIdleRight, bufferIdleRight.length, group, MULTICAST_PORT);
            sendSocket.send(idlePacket);
            System.out.println("Sent to Multicast: "+idleCheck);

            byte[] bufferReceiveIdle = new byte[256];
            DatagramPacket receivePacketIdle = new DatagramPacket(bufferReceiveIdle, bufferReceiveIdle.length);
            socket.receive(receivePacketIdle);
            String receiveLogin = new String(receivePacketIdle.getData(), 0, receivePacketIdle.getLength());
            System.out.println("Received from Multicast: "+receiveLogin);

            if(receiveLogin.equals("type|idle;rights|editor")){
                return true;
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized String checkLogin(String username, String password) {
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
                return "error";
            }
            else if(receiveLogin.equals("type|status;logged|on;msg|WelcomeToDropMusic|privilege;editor|")){
                return "editor";
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            socket.close();
            sendSocket.close();
        }
        return "user";
    }

    public synchronized boolean checkArtist(String artistName, String description) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String stringArtist = "type|insert_artist;" + artistName + ";description|" + description;
            System.out.println(stringArtist);
            byte[] bufferLogin = stringArtist.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferLogin, bufferLogin.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: "+stringArtist);

            byte[] bufferReceiveLogin = new byte[256];
            DatagramPacket receivePacketLogin = new DatagramPacket(bufferReceiveLogin, bufferReceiveLogin.length);
            socket.receive(receivePacketLogin);
            String receiveLogin = new String(receivePacketLogin.getData(), 0, receivePacketLogin.getLength());
            System.out.println("Received from Multicast: "+receiveLogin);
            if(receiveLogin.equals("type|insert_artist;successful")){
                return true;
            }
            else if(receiveLogin.equals("type|insert_artist;error in insert artist")){
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
        return false;
    }

    public synchronized boolean checkAlbum(String albumName, String description,String artistName) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);


            String stringAlbum = "type|insert_album;album_name|" + albumName + ";description|" + description +";artist_name|" + artistName;
            System.out.println(stringAlbum);
            byte[] bufferAlbum = stringAlbum.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferAlbum, bufferAlbum.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: "+stringAlbum);

            byte[] bufferReceiveAlbum = new byte[256];
            DatagramPacket receivePacketAlbum = new DatagramPacket(bufferReceiveAlbum, bufferReceiveAlbum.length);
            socket.receive(receivePacketAlbum);
            String receiveAlbum = new String(receivePacketAlbum.getData(), 0, receivePacketAlbum.getLength());
            System.out.println("Received from Multicast: "+receiveAlbum);
            if(receiveAlbum.equals("type|insert_album;successful")){
                return true;
            }
            else if(receiveAlbum.equals("type|insert_album;error in insert album")){
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
        return false;
    }

    public synchronized boolean checkMusic(String musicName, String genre,String duration, String artistName, String albumName) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);


            String stringMusic = "type|insert_music;music_name|" + musicName + ";genre|"+ genre +  ";duration|" + duration +";artist_name|" + artistName + ";album_name|" + albumName;
            System.out.println(stringMusic);
            byte[] bufferMusic = stringMusic.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferMusic, bufferMusic.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: "+stringMusic);

            byte[] bufferReceiveMusic = new byte[256];
            DatagramPacket receivePacketMusic = new DatagramPacket(bufferReceiveMusic, bufferReceiveMusic.length);
            socket.receive(receivePacketMusic);
            String receiveAlbum = new String(receivePacketMusic.getData(), 0, receivePacketMusic.getLength());
            System.out.println("Received from Multicast: "+receiveAlbum);
            if(receiveAlbum.equals("type|insert_music;successful")){
                return true;
            }
            else if(receiveAlbum.equals("type|insert_music;error in insert music")){
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
        return false;
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