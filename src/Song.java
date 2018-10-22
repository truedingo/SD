import java.io.Serializable;

public class Song implements Serializable{
    private static final long serialVersionUID = 1L;
    private String songName;
    private String songGenre;
    private String duration;
    private String releaseDate;
    private String lyrics;



    Song(){
    };

    public Song(String songName, String songGenre, String duration, String releaseDate, String lyrics) {
        this.songName = songName;
        this.songGenre = songGenre;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.lyrics = lyrics;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    @Override
    public String toString() {
        return "Name:" + songName + "\nGenre:" + songGenre + "\nDuration:" + duration;
    }
}
