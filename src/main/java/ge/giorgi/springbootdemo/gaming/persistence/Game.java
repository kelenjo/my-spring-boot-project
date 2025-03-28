package ge.giorgi.springbootdemo.gaming.persistence;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "game")
@SequenceGenerator(name = "game_seq_gen", sequenceName = "game_seq", allocationSize = 1)
@Getter
@Setter
public class Game {

    @Id
    @GeneratedValue(generator = "game_seq_gen", strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name="title")
    private String title;

    @Column(name="genre")
    private String genre;

    @Column(name="release_year")
    private int releaseYear;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

//    public long getId() {
//        return id;
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
//        return releaseYear;
//    }
//
//    public void setReleaseYear(int releaseYear) {
//        this.releaseYear = releaseYear;
//    }
//
//    public Company getCompany() {
//        return company;
//    }
//    public void setCompany(Company company){
//        this.company=company;
//    }
}
