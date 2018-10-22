
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

    public synchronized boolean checkAlbum(String albumName, String description,String musicalGenre, String udate, String artistName) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            String stringAlbum = "type|insert_album;album_name|" + albumName + ";description|" + description +";music_genre|"+musicalGenre+";date|"+udate+";artist_name|"+artistName;
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

    public synchronized boolean checkMusic(String musicName, String genre,String duration, String udate, String lyrics, String artistName, String albumName) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);


            String stringMusic = "type|insert_music;music_name|" + musicName + ";genre|"+ genre + ";duration|" + duration +";artist_name|" + artistName + ";album_name|" + albumName+";lyrics|"+lyrics+";date|"+udate;
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

    public synchronized boolean checkRemoveMusic(String musicName, String artistName, String albumName) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);


            String stringRemoveMusic = "type|remove_music;music_name|" + musicName + ";artist_name|" + artistName + ";album_name|" + albumName;
            System.out.println(stringRemoveMusic);
            byte[] bufferRemoveMusic = stringRemoveMusic.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferRemoveMusic, bufferRemoveMusic.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: "+stringRemoveMusic);

            byte[] bufferReceiveRemoveMusic = new byte[256];
            DatagramPacket receivePacketRemoveMusic = new DatagramPacket(bufferReceiveRemoveMusic, bufferReceiveRemoveMusic.length);
            socket.receive(receivePacketRemoveMusic);
            String receiveAlbum = new String(receivePacketRemoveMusic.getData(), 0, receivePacketRemoveMusic.getLength());
            System.out.println("Received from Multicast: "+receiveAlbum);
            if(receiveAlbum.equals("type|remove_music;successful")){
                return true;
            }
            else if(receiveAlbum.equals("type|remove_music;error in remove music")){
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

    public synchronized boolean checkRemoveAlbum(String artistName, String albumName) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);


            String stringRemoveAlbum = "type|remove_album;album_name|" + albumName + ";artist_name|" + artistName;
            System.out.println(stringRemoveAlbum);
            byte[] bufferRemoveAlbum = stringRemoveAlbum.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferRemoveAlbum, bufferRemoveAlbum.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: "+stringRemoveAlbum);

            byte[] bufferReceiveRemoveAlbum = new byte[256];
            DatagramPacket receivePacketRemoveAlbum= new DatagramPacket(bufferReceiveRemoveAlbum, bufferReceiveRemoveAlbum.length);
            socket.receive(receivePacketRemoveAlbum);
            String receiveAlbum = new String(receivePacketRemoveAlbum.getData(), 0, receivePacketRemoveAlbum.getLength());
            System.out.println("Received from Multicast: "+receiveAlbum);
            if(receiveAlbum.equals("type|remove_album;successful")){
                return true;
            }
            else if(receiveAlbum.equals("type|remove_album;error in remove album")){
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

    public synchronized boolean checkRemoveArtist(String artistName) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);


            String stringRemoveAlbum = "type|remove_artist;artist_name|" + artistName;
            System.out.println(stringRemoveAlbum);
            byte[] bufferRemoveAlbum = stringRemoveAlbum.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferRemoveAlbum, bufferRemoveAlbum.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: "+stringRemoveAlbum);

            byte[] bufferReceiveRemoveAlbum = new byte[256];
            DatagramPacket receivePacketRemoveAlbum= new DatagramPacket(bufferReceiveRemoveAlbum, bufferReceiveRemoveAlbum.length);
            socket.receive(receivePacketRemoveAlbum);
            String receiveAlbum = new String(receivePacketRemoveAlbum.getData(), 0, receivePacketRemoveAlbum.getLength());
            System.out.println("Received from Multicast: "+receiveAlbum);
            if(receiveAlbum.equals("type|remove_artist;successful")){
                return true;
            }
            else if(receiveAlbum.equals("type|remove_artist;error in remove artist")){
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

    public synchronized boolean checkEditArtist(String oldArtistName, String newArtistName, String newDesc) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String stringEditArtist = "type|edit_artist;old_artist_name|" + oldArtistName + ";new_artist_name|" + newArtistName + ";new_description|" + newDesc;
            System.out.println(stringEditArtist);
            byte[] bufferEditArtist = stringEditArtist.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferEditArtist, bufferEditArtist.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringEditArtist);

            byte[] bufferReceiveEditArtist = new byte[256];
            DatagramPacket receiveEditArtistPacket = new DatagramPacket(bufferReceiveEditArtist, bufferReceiveEditArtist.length);
            socket.receive(receiveEditArtistPacket);
            String receiveEditArtist = new String(receiveEditArtistPacket.getData(), 0, receiveEditArtistPacket.getLength());
            System.out.println("Received from Multicast: " + receiveEditArtist);

            if (receiveEditArtist.equals("type|edit_artist;successful")) {
                return true;
            } else if (receiveEditArtist.equals("type|edit_artist;error in edit artist")) {
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
        return false;
    }

    public synchronized boolean checkEditAlbum(String artistName, String oldAlbumName, String newAlbumName, String albumDescr, String musicalGenre, String udate){

        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String stringEditAlbum = "type|edit_album;artist_name|" + artistName + ";old_album_name|" + oldAlbumName+";new_album_name|"+newAlbumName+";new_description|"+albumDescr+";music_genre|"+musicalGenre+";date|"+udate;
            System.out.println(stringEditAlbum);
            byte[] bufferEditAlbum = stringEditAlbum.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferEditAlbum, bufferEditAlbum.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringEditAlbum);

            byte[] bufferReceiveEditAlbum = new byte[256];
            DatagramPacket receiveEditAlbumPacket = new DatagramPacket(bufferReceiveEditAlbum, bufferReceiveEditAlbum.length);
            socket.receive(receiveEditAlbumPacket);
            String receiveEditAlbum = new String(receiveEditAlbumPacket.getData(), 0, receiveEditAlbumPacket.getLength());
            System.out.println("Received from Multicast: " + receiveEditAlbum);

            if (receiveEditAlbum.equals("type|edit_album;successful")) {
                return true;
            } else if (receiveEditAlbum.equals("type|edit_album;error in edit album")) {
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
        return false;
    }

    public synchronized boolean checkEditSong(String artistName, String albumName, String oldMusicName, String newMusicName, String musicGenre, String duration, String date, String lyrics) {

        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String stringEditSong = "type|edit_music;artist_name|" + artistName + ";album_name|" + albumName + ";old_music_name|" + oldMusicName + ";new_music_name|" + newMusicName + ";music_genre|" + musicGenre + ";duration|" + duration + ";date|" + date + ";lyrics|" + lyrics;
            System.out.println(stringEditSong);
            byte[] bufferEditSong = stringEditSong.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferEditSong, bufferEditSong.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringEditSong);

            byte[] bufferReceiveEditSong = new byte[256];
            DatagramPacket receiveEditSongPacket = new DatagramPacket(bufferReceiveEditSong, bufferReceiveEditSong.length);
            socket.receive(receiveEditSongPacket);
            String receiveEditSong = new String(receiveEditSongPacket.getData(), 0, receiveEditSongPacket.getLength());
            System.out.println("Received from Multicast: " + receiveEditSong);

            if (receiveEditSong.equals("type|edit_music;successful")) {
                return true;
            } else if (receiveEditSong.equals("type|edit_music;error in edit album")) {
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