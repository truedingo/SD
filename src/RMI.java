import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.rmi.*;
public interface RMI extends Remote {
    String sayHello() throws java.rmi.RemoteException;

    boolean checkRegister(String username, String password) throws java.rmi.RemoteException;

    String checkLogin(String username, String password) throws java.rmi.RemoteException;

    boolean checkUserRights(String username, ClientInterface client) throws java.rmi.RemoteException;

    boolean checkArtist(String artistName, String description) throws java.rmi.RemoteException;

    boolean checkAlbum(String albumName, String description, String musicalGenre, String udate, String artistName) throws java.rmi.RemoteException;

    boolean checkMusic(String musicName, String genre, String duration, String udate, String lyrics, String artistName, String albumName) throws java.rmi.RemoteException;

    boolean checkRemoveMusic(String musicName, String artistName, String albumName) throws java.rmi.RemoteException;

    boolean checkRemoveAlbum(String artistName, String albumName) throws java.rmi.RemoteException;

    boolean checkRemoveArtist(String artistName) throws java.rmi.RemoteException;

    boolean checkEditArtist(String oldArtistName, String newArtistName, String newDesc) throws java.rmi.RemoteException;

    boolean checkEditAlbum(String artistName, String oldAlbumName, String newAlbumName, String albumDescr, String musicalGenre, String udate) throws java.rmi.RemoteException;

    boolean checkEditSong(String artistName, String albumName, String oldMusicName, String newMusicName, String musicGenre, String duration, String date, String lyrics) throws java.rmi.RemoteException;

    boolean checkCritic(String username, String artistName, String albumName, int rate, String critic) throws java.rmi.RemoteException;

    String checkFromAlbumName(String username, String albumName) throws RemoteException;

    String checkFromArtistName(String username, String artistName) throws RemoteException;

    String checkViewArtistDetails(String username, String artistName) throws RemoteException;

    String checkViewAlbumDetails(String username, String artistName, String albumName) throws RemoteException;

    String checkViewAlbumCritics(String username, String artistName, String albumName) throws RemoteException;

    void addLoggedUsers(ClientInterface c) throws java.rmi.RemoteException;

    String removeLoggedUsers(String username) throws RemoteException;

    boolean searchOnlineListRights(String username) throws RemoteException;

    ClientInterface fetchClientOnlineListRighs(String username) throws RemoteException;

    boolean checkNotificationsRights(String username, ClientInterface client) throws  RemoteException;

    String checkViewSongDetails(String username, String artistName, String albumName, String song) throws RemoteException;

    int checkUpload(String username, String musicPath,String albumName, String artistName, String musicName) throws RemoteException;

    int checkDownload(String username, String musicPath,String albumName, String artistName, String musicName);




    }