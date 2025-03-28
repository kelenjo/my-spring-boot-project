package ge.giorgi.springbootdemo.gaming.models;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameWithoutCompanyDTO {
    private long id;
    private String title;
    private String genre;
    private int release_year;

    public GameWithoutCompanyDTO() {
    }

//    public GameWithoutCompanyDTO(long id, String title, String genre, int release_year) {
//        this.id = id;
//        this.title = title;
//        this.genre = genre;
//        this.release_year = release_year;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
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
//    public int getReleaseYear() {
//        return release_year;
//    }
//
//    public void setReleaseYear(int release_year) {
//        this.release_year = release_year;
//    }
}
