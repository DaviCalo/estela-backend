package dev.smd.estela.backend.service;

import static dev.smd.estela.backend.config.Config.PATH_FILES_GAMES;
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
            String extensionCover = coverPart.getSubmittedFileName().substring(coverPart.getSubmittedFileName().lastIndexOf("."));
            String extensionIcon = iconPart.getSubmittedFileName().substring(iconPart.getSubmittedFileName().lastIndexOf("."));
            Media converGameMedia = new Media(converGame + extensionCover, "cover", gameSaved.getGameId());
            Media iconGameMedia = new Media(IconGame + extensionIcon, "icon", gameSaved.getGameId());
            isSucess = mediaDao.save(converGameMedia);
            saveFile(coverPart, converGame);
            isSucess = mediaDao.save(iconGameMedia);
            saveFile(iconPart, IconGame);
            for (int i = 0; i < listOfMidias.size(); i++) {
                Part filePart = listOfMidias.get(i);
                String originalName = filePart.getSubmittedFileName();
                String extension = "";
                if (originalName != null && originalName.contains(".")) {
                    extension = originalName.substring(originalName.lastIndexOf("."));
                }
                String fileName = "midia_game_" + (i + 1) + "_" + gameSaved.getGameId();
                Media mediaGameMedia = new Media(fileName + extension, "media", gameSaved.getGameId());
                boolean savedInDb = mediaDao.save(mediaGameMedia);
                if (savedInDb) {
                    saveFile(filePart, fileName);
                }
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
            String uploadPath = PATH_FILES_GAMES;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            File finalFile = new File(uploadDir, newFileName);
            String filePath = finalFile.getAbsolutePath();
            filePart.write(filePath);
            System.out.println(filePath);
        } catch (IOException e) {
            System.out.println(e.getCause());
        }
    }
}
