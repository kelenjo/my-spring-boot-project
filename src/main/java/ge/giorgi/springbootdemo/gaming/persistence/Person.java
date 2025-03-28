package ge.giorgi.springbootdemo.gaming.persistence;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="person")
@SequenceGenerator(name="person_seq_gen", sequenceName = "person_seq", allocationSize = 1)
@Getter
@Setter
public class Person {

    @Id
    @GeneratedValue(generator = "person_seq_gen", strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name="name")
    private String name;

    @Column(name = "age")
    private int age;

//    public long getId(){
//        return id;
//    }
//
//    public String getName(){
//        return name;
//    }
//
//    public void setName(String name){
//        this.name=name;
//    }
//
//    public int getAge(){
//        return age;
//    }
//
//    public void setAge(int age){
//        this.age=age;
//    }




}
