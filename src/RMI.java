import java.rmi.*;
public interface RMI extends Remote {
	String sayHello() throws java.rmi.RemoteException;
	void insertUser(User user) throws RemoteException;
	boolean verificaUserExiste(String user) throws RemoteException;
	boolean verifyCredencials(String username, String password) throws RemoteException;
	}