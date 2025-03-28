package ge.giorgi.springbootdemo.gaming;

import ge.giorgi.springbootdemo.gaming.models.CompanyDTO;
import ge.giorgi.springbootdemo.gaming.models.GameDTO;
import ge.giorgi.springbootdemo.gaming.models.PersonDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gaming")
public class GamingController {


    private final GamingService gamingService;

    public GamingController(GamingService gamingService){
        this.gamingService=gamingService;
    }

}
