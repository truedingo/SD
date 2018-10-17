import java.io.Serializable;

public class Song implements Serializable{
    private static final long serialVersionUID = 1L;
    private String songName;
    private String songGenre;
    private String album;
    private String artist;
    private long duration;


    Song(){
    };

    public Song(String songName, String songGenre, String album, String artist, long duration) {
        this.songName = songName;
        this.songGenre = songGenre;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongGenre() {
        return songGenre;
    }

    public void setSongGenre(String songGenre) {
        this.songGenre = songGenre;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Name:" + songName + "\nGenre:" + songGenre + "\nAlbum:" + album + "\nArtist:" + artist + "\nDuration:" + duration;
    }
}
