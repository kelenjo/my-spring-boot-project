package ge.giorgi.springbootdemo.gaming.models;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonDTO {
    private long id;
    private String name;
    private int age;

//    public PersonDTO(long id, String name, int age) {
//        this.id=id;
//        this.name = name;
//        this.age = age;
//    }
//    public long getId() {
//        return id;
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
//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        if (age > 0) {
//            this.age = age;
//        } else {
//            throw new IllegalArgumentException("Age must be positive");
//        }
//    }
//
//    // toString Method
//    @Override
//    public String toString() {
//        return "Person{" + "name='" + name + '\'' + ", age=" + age + '}';
//    }
}

