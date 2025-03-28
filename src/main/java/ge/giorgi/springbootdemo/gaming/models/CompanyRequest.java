package ge.giorgi.springbootdemo.gaming.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyRequest {
    private String name;
    private Long ownerId;

//    public CompanyRequest(){}
//
//    public CompanyRequest(String name, Long ownerId) {
//        this.name = name;
//        this.ownerId = ownerId;
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
//    public Long getOwnerId() {
//        return ownerId;
//    }
//
//    public void setOwnerId(Long ownerId) {
//        this.ownerId = ownerId;
//    }



}
