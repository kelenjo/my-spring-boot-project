package ge.giorgi.springbootdemo.gaming.models;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameDTO {
    private long id;
    private String title;
    private String genre;
    private int releaseYear;
    private long companyId;

//    // Constructor
//    public GameDTO(long id, String title, String genre, int releaseYear, long companyId) {
//        this.id=id;
//        this.title = title;
//        this.genre = genre;
//        this.releaseYear = releaseYear;
//        this.companyId=companyId;
//    }
//    public long getId(){
//        return id;
//    }
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getGenre() {
//        return genre;
//    }
//
//    public void setGenre(String genre) {
//        this.genre = genre;
//    }
//
//    public long getCompanyId() {
//        return companyId;
//    }
//
//    public int getReleaseYear() {
//        return releaseYear;
//    }
//
//    public void setReleaseYear(int releaseYear) {
//        if (releaseYear > 1900) {
//            this.releaseYear = releaseYear;
//        } else {
//            throw new IllegalArgumentException("Invalid release year");
//        }
//    }
//    @Override
//    public String toString() {
//        return "Game{" + "title='" + title + '\'' + ", genre='" + genre + '\'' + ", releaseYear=" + releaseYear + '}';
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if(obj instanceof GameDTO other)
//            return this.id==other.getId();
//        return false;
//    }
}