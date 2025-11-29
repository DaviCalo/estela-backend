package dev.smd.estela.backend.service;

import dev.smd.estela.backend.dao.GameDAO;
import dev.smd.estela.backend.dto.ReponseGamesDTO;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GameService {

        GameDAO gameDao = new GameDAO();

        public ArrayList<ReponseGamesDTO> listAllGames() {
                ArrayList<ReponseGamesDTO> gamesList = null;

                gamesList = gameDao.findAllGamesWithCover().stream().map(game
                          -> new ReponseGamesDTO(game.getGameId(), game.getName(), game.getPrice(), game.getUrlCover(), game.getCreatedAt())
                ).collect(Collectors.toCollection(ArrayList::new));

                return gamesList;
        }
}
