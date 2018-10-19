import java.util.ArrayList;

public class Album {

    private static final long serialVersionUID = 1L;
    private String albumName;
    ArrayList<Integer> mediaRating;
    ArrayList<Critic> critics;
    ArrayList<Song> songs;
    ArrayList<String> userChanges;

    Album(){};

    public Album(String albumName) {
        this.albumName = albumName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public ArrayList<Integer> getMediaRating() {
        return mediaRating;
    }

    public void setMediaRating(ArrayList<Integer> mediaRating) {
        this.mediaRating = mediaRating;
    }

    public ArrayList<Critic> getCritics() {
        return critics;
    }

    public void setCritics(ArrayList<Critic> critics) {
        this.critics = critics;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public ArrayList<String> getUserChanges() {
        return userChanges;
    }

    public void setUserChanges(ArrayList<String> userChanges) {
        this.userChanges = userChanges;
    }

    @Override
    public String toString() {
        return "Album:" + albumName + "\nAvgRating:" + mediaRating + "\nCritics:" + critics + "\nSong:" + songs + "\nUserChanges:" + userChanges;
    }
}
