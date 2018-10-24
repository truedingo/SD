import java.rmi.*;
public interface ClientInterface extends Remote {

    String getUsername() throws java.rmi.RemoteException;
    void setUsername(String username) throws java.rmi.RemoteException;
    void notifyRights() throws RemoteException;



}
