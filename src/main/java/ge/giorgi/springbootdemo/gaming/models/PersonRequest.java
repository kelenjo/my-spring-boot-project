package ge.giorgi.springbootdemo.gaming.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonRequest {
    private String name;
    private int age;

//    // Constructor
//    public PersonRequest(){}
//
//    public PersonRequest(String name, int age) {
//        this.name = name;
//        this.age = age;
//    }
//
//    // Getters and Setters
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
//        this.age = age;
//    }
}

