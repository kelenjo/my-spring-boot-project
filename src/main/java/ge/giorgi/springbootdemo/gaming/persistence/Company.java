package ge.giorgi.springbootdemo.gaming.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="company")
@SequenceGenerator(name="company_seq_gen", sequenceName = "company_seq", allocationSize = 1)
@Getter
@Setter
public class Company {

    @Id
    @GeneratedValue(generator = "company_seq_gen", strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Person owner;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Game> games = new ArrayList<>(); // akavshirebs tamashebs kompaniastan

//    public Person getOwner() {
//        return owner;
//    }
//
//    public void setOwner(Person owner) {
//        this.owner = owner;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public List<Game> getGames() {
//        return games;
//    }
//
//    public void setGames(List<Game> games) {
//        this.games = games;
//    }

}
