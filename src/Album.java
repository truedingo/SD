import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Album {

    private static final long serialVersionUID = 1L;
    private String albumName;
    private String description;
    CopyOnWriteArrayList<Integer> mediaRating;
    CopyOnWriteArrayList<Critic> critics;
    CopyOnWriteArrayList<Song> songs;
    CopyOnWriteArrayList<String> userChanges;

    Album(){};

    public Album(String albumName, String description) {
        this.albumName = albumName;
        this.description = description;

        this.songs = new CopyOnWriteArrayList<Song>();
    }

    public void setSongs(CopyOnWriteArrayList<Song> songs) {
        this.songs = songs;
    }

    public void addSongs( Song song) {
        this.songs.add(song);
    }

    public void removeSongs( Song song) {
        this.songs.remove(song);
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

    public CopyOnWriteArrayList<Integer> getMediaRating() {
        return mediaRating;
    }

    public void setMediaRating(CopyOnWriteArrayList<Integer> mediaRating) {
        this.mediaRating = mediaRating;
    }

    public CopyOnWriteArrayList<Critic> getCritics() {
        return critics;
    }

    public void setCritics(CopyOnWriteArrayList<Critic> critics) {
        this.critics = critics;
    }

    public CopyOnWriteArrayList<Song> getSongs() {
        return songs;
    }

    public CopyOnWriteArrayList<String> getUserChanges() {
        return userChanges;
    }

    public void setUserChanges(CopyOnWriteArrayList<String> userChanges) {
        this.userChanges = userChanges;
    }

    @Override
    public String toString() {
        return "Album:" + albumName + "\nAvgRating:" + mediaRating + "\nCritics:" + critics + "\nSong:" + songs + "\nUserChanges:" + userChanges;
    }
}
