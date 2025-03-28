package ge.giorgi.springbootdemo.gaming;

import ge.giorgi.springbootdemo.gaming.models.GameDTO;
import ge.giorgi.springbootdemo.gaming.models.GameRequest;
import ge.giorgi.springbootdemo.gaming.models.GameWithoutCompanyDTO;
import ge.giorgi.springbootdemo.gaming.persistence.Game;
import ge.giorgi.springbootdemo.gaming.persistence.GameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameWithoutCompanyDTO addGame(Game gameToSave){
        gameRepository.save(gameToSave);
        return new GameWithoutCompanyDTO(gameToSave.getId(), gameToSave.getTitle(), gameToSave.getGenre(), gameToSave.getReleaseYear());
    }
    public GameDTO mapGame(Game game){
        return new GameDTO(
                game.getId(),
                game.getTitle(),
                game.getGenre(),
                game.getReleaseYear(),
                game.getCompany().getId()
        );
    }
//    public List<GameDTO> mapGames(List<Game> games) {
//        return games.stream()
//                .map(game -> new GameDTO(
//                        game.getId(),
//                        game.getTitle(),
//                        game.getGenre(),
//                        game.getReleaseYear(),
//                        game.getCompany().getId()
//                ))
//                .collect(Collectors.toList());
//    }

    public List<GameWithoutCompanyDTO> mapToGameWithoutCompanyDTO(List<Game> games) {
        return games.stream()
                .map(game -> new GameWithoutCompanyDTO(
                        game.getId(),
                        game.getTitle(),
                        game.getGenre(),
                        game.getReleaseYear()
                ))
                .collect(Collectors.toList());
    }

    public GameDTO findGame(long id){
        Game game=gameRepository.findById(id).orElse(null);
        if(game!=null){
            return mapGame(game);
        }
        return null;
    }

//    public void deleteCompanyGames(long companyId){
//        gameRepository.deleteByCompanyId(companyId);
//    }

    public boolean deleteGame(long gameId){
        Game game=gameRepository.findById(gameId).orElse(null);
        if(game!=null){
            gameRepository.delete(game);
            return true;
        }
        return false;
    }

    public List<GameWithoutCompanyDTO> getGamesByCompanyId(long companyId) {
        return gameRepository.findGamesByCompanyId(companyId);
    }

    public Page<GameDTO> getGames(int page, int pageSize){
        return gameRepository.findAllGames(PageRequest.of(page, pageSize));
    }
}
