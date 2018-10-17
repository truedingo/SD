import java.io.Serializable;
import java.util.ArrayList;

public class Artist implements Serializable{

    private static final long serialVersionUID = 1L;
    private String artistName;
    private String descArtist;
    private ArrayList<Album> albums;
    private ArrayList<String> userChanges;

    Artist(){};

    public Artist(String artistName, String descArtist, ArrayList<Album> albums, ArrayList<String> userChanges) {
        this.artistName = artistName;
        this.descArtist = descArtist;
        this.albums = albums;
        this.userChanges = userChanges;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getDescArtist() {
        return descArtist;
    }

    public void setDescArtist(String descArtist) {
        this.descArtist = descArtist;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

    public ArrayList<String> getUserChanges() {
        return userChanges;
    }

    public void setUserChanges(ArrayList<String> userChanges) {
        this.userChanges = userChanges;
    }

    @Override
    public String toString() {
        return "Artist:" + artistName + "\nDescArtist:" + descArtist + "\nAlbums:" + albums + "\nUserChanges:" + userChanges;
    }
}
