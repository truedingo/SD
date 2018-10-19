import java.rmi.*;
public interface RMI extends Remote {
	String sayHello() throws java.rmi.RemoteException;
	boolean checkRegister(String username, String password) throws java.rmi.RemoteException;
	String checkLogin(String username, String password) throws java.rmi.RemoteException;
	public boolean checkArtist(String artistName, String description) throws java.rmi.RemoteException;
	}