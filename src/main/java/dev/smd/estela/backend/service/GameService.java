package dev.smd.estela.backend.service;

import static dev.smd.estela.backend.config.Config.PATH_FILES_USERS;
import dev.smd.estela.backend.dao.GameDAO;
import dev.smd.estela.backend.dao.MediaDAO;
import dev.smd.estela.backend.dto.ReponseGamesDTO;
import dev.smd.estela.backend.model.Game;
import dev.smd.estela.backend.model.Media;
import jakarta.servlet.http.Part;
import jakarta.ws.rs.Path;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameService {

        private static final GameDAO gameDao = new GameDAO();
        private static final MediaDAO mediaDao = new MediaDAO();

        public ArrayList<ReponseGamesDTO> listAllGames() {
                ArrayList<ReponseGamesDTO> gamesList = null;

                gamesList = gameDao.findAllGamesWithCover().stream().map(game
                          -> new ReponseGamesDTO(game.getGameId(), game.getName(), game.getPrice(), game.getUrlCover(), game.getCreatedAt())
                ).collect(Collectors.toCollection(ArrayList::new));

                return gamesList;
        }

        public boolean saveGame(BigDecimal price, String name, String characteristics, String description, String hardDriveSpace, String graphicsCard, String memory, String operatingSystem, String processor, Part coverPart, Part iconPart, List<Part> listOfMidias) {
                Game newGame = new Game(null, price, name, characteristics, null, description, hardDriveSpace, graphicsCard, memory, operatingSystem, processor, null);
                boolean isSucess = gameDao.save(newGame);

                if (isSucess) {
                        Game gameSaved = gameDao.getByName(name);
                        String converGame = "cover_game_" + gameSaved.getGameId();
                        String IconGame = "icon_game_" + gameSaved.getGameId();
                        Media converGameMedia = new Media(converGame, "cover_game", gameSaved.getGameId());
                        Media iconGameMedia = new Media(IconGame, "icon_game", gameSaved.getGameId());
                        isSucess = mediaDao.save(converGameMedia);
                        saveFile(coverPart, converGame);
                        isSucess = mediaDao.save(iconGameMedia);
                        saveFile(iconPart, IconGame);
                        for (Part path : listOfMidias) {
                                String fileName =  "midia_game_" + gameSaved.getGameId();
                                Media mediaGameMedia = new Media(fileName, "media_game", gameSaved.getGameId());
                                isSucess = mediaDao.save(mediaGameMedia);
                                saveFile(path, fileName);
                        }
                }

                return isSucess;
        }

        private void saveFile(Part filePart, String fileName) {
                try {
                        String fileExtension = "";
                        String submittedFileName = filePart.getSubmittedFileName();
                        int dotIndex = submittedFileName.lastIndexOf('.');
                        if (dotIndex > 0 && dotIndex < submittedFileName.length() - 1) {
                                fileExtension = submittedFileName.substring(dotIndex);
                        }
                        String newFileName = fileName + fileExtension;
                        String uploadPath = PATH_FILES_USERS;
                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists()) {
                                uploadDir.mkdirs();
                        }
                        String filePath = uploadPath + File.separator + newFileName;
                        filePart.write(filePath);
                } catch (IOException e){
                        System.out.println(e.getCause());
                }
        }
}
