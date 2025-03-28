package ge.giorgi.springbootdemo.gaming;

import ge.giorgi.springbootdemo.gaming.models.GameDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService){
        this.gameService=gameService;
    }

    @GetMapping
    public Page<GameDTO> getGames(@RequestParam int page, int pageSize){
        return gameService.getGames(page, pageSize);
    }

    @GetMapping("{id}")
    public ResponseEntity<GameDTO> getGame(@PathVariable long id){
        GameDTO gameDTO=gameService.findGame(id);
        if(gameDTO!=null){
            return ResponseEntity.ok(gameDTO);
        }
        return ResponseEntity.notFound().build();
    }
}
