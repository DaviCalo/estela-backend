package dev.smd.estela.backend.service;

import static dev.smd.estela.backend.config.Config.PATH_FILES_GAMES;
import static dev.smd.estela.backend.config.Config.PATH_FILES_USERS;
import dev.smd.estela.backend.dao.GameDAO;
import dev.smd.estela.backend.dao.MediaDAO;
import dev.smd.estela.backend.dto.ReponseGameDetailsDTO;
import dev.smd.estela.backend.dto.ReponseGamesDTO;
import dev.smd.estela.backend.dto.ReponseGamesDetailsDTO;
import dev.smd.estela.backend.model.Game;
import dev.smd.estela.backend.model.Media;
import jakarta.servlet.http.Part;
import jakarta.ws.rs.Path;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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

    public ArrayList<ReponseGamesDetailsDTO> listAllGamesDetails() {
        ArrayList<ReponseGamesDetailsDTO> gamesList = null;

        gamesList = gameDao.findAllGamesDetails().stream().map(game
                -> new ReponseGamesDetailsDTO(game.getGameId(), game.getName(), game.getPrice(), game.getDescription(), game.getCreatedAt(), game.getSold())
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

    public boolean updateGame(Long gameId, BigDecimal price, String name, String characteristics,
            String description, String hardDriveSpace, String graphicsCard,
            String memory, String operatingSystem, String processor,
            Part coverPart, Part iconPart, List<Part> listOfMidias,
            String categoryIdsParam) {

        // 1. Atualizar dados textuais do jogo
        Game gameToUpdate = new Game();
        gameToUpdate.setGameId(gameId);
        gameToUpdate.setPrice(price);
        gameToUpdate.setName(name);
        gameToUpdate.setCharacteristics(characteristics);
        gameToUpdate.setDescription(description);
        gameToUpdate.setHardDriveSpace(hardDriveSpace);
        gameToUpdate.setGraphicsCard(graphicsCard);
        gameToUpdate.setMemory(memory);
        gameToUpdate.setOperatingSystem(operatingSystem);
        gameToUpdate.setProcessor(processor);

        boolean isSuccess = gameDao.update(gameToUpdate);

        if (isSuccess) {
            try {
                // 2. Atualizar Categorias (Se enviadas)
                if (categoryIdsParam != null && !categoryIdsParam.isEmpty()) {
                    gameDao.clearCategories(gameId);
                    String[] ids = categoryIdsParam.split(",");
                    for (String idStr : ids) {
                        try {
                            gameDao.addCategory(gameId, Long.parseLong(idStr.trim()));
                        } catch (NumberFormatException e) {
                            System.out.println("ID categoria inválido: " + idStr);
                        }
                    }
                }

                if (coverPart != null && coverPart.getSize() > 0) {
                    mediaDao.deleteByGameIdAndType(gameId, "cover");

                    String ext = getExtension(coverPart);
                    String fileName = "cover_game_" + gameId + ext;

                    mediaDao.save(new Media(fileName, "cover", gameId));
                    saveFile(coverPart, "cover_game_" + gameId);
                }

                if (iconPart != null && iconPart.getSize() > 0) {
                    mediaDao.deleteByGameIdAndType(gameId, "icon");

                    String ext = getExtension(iconPart);
                    String fileName = "icon_game_" + gameId + ext;

                    mediaDao.save(new Media(fileName, "icon", gameId));
                    saveFile(iconPart, "icon_game_" + gameId);
                }

                boolean hasGalleryUpdate = listOfMidias.stream().anyMatch(p -> p != null && p.getSize() > 0);

                if (hasGalleryUpdate) {
                    mediaDao.deleteByGameIdAndType(gameId, "media"); // Limpa galeria antiga

                    for (int i = 0; i < listOfMidias.size(); i++) {
                        Part filePart = listOfMidias.get(i);
                        if (filePart != null && filePart.getSize() > 0) {
                            String ext = getExtension(filePart);
                            String fileName = "midia_game_" + (i + 1) + "_" + gameId + ext;

                            mediaDao.save(new Media(fileName, "media", gameId));
                            saveFile(filePart, "midia_game_" + (i + 1) + "_" + gameId);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    private String getExtension(Part part) {
        String name = part.getSubmittedFileName();
        return (name != null && name.contains(".")) ? name.substring(name.lastIndexOf(".")) : "";
    }

    public boolean deleteGame(Long gameId) {
        return gameDao.deleteById(gameId);
    }

    public ReponseGameDetailsDTO getDeitalsGameById(Long gameId) {
        return gameDao.getGameDetailsById(gameId);
    }
}
