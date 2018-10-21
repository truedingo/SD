import java.rmi.*;
public interface RMI extends Remote {
	String sayHello() throws java.rmi.RemoteException;
	boolean checkRegister(String username, String password) throws java.rmi.RemoteException;
	String checkLogin(String username, String password) throws java.rmi.RemoteException;
	boolean checkUserRights(String username) throws java.rmi.RemoteException;
	boolean idleUserRights(String username) throws java.rmi.RemoteException;
    boolean checkArtist(String artistName, String description) throws java.rmi.RemoteException;
    boolean checkAlbum(String albumName, String description,String artistName) throws java.rmi.RemoteException;
    boolean checkMusic(String musicName, String genre,String duration, String artistName, String albumName) throws  java.rmi.RemoteException;
    boolean checkRemoveMusic(String musicName, String artistName, String albumName) throws java.rmi.RemoteException;
    boolean checkRemoveAlbum(String artistName, String albumName) throws java.rmi.RemoteException;
    boolean checkRemoveArtist(String artistName) throws java.rmi.RemoteException;
    public boolean checkEditArtist(String oldArtistName, String newArtistName, String newDesc) throws java.rmi.RemoteException;
	}