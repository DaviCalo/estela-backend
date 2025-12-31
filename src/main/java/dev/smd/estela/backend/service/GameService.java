package dev.smd.estela.backend.service;

import static dev.smd.estela.backend.config.Config.PATH_FILES_GAMES;
import dev.smd.estela.backend.dao.CategoriesGamesDAO;
import dev.smd.estela.backend.dao.GameDAO;
import dev.smd.estela.backend.dao.MediaDAO;
import dev.smd.estela.backend.dto.game.ReponseGameDetailsDTO;
import dev.smd.estela.backend.dto.game.ReponseGamePurchasedDTO;
import dev.smd.estela.backend.dto.game.ReponseGamesDTO;
import dev.smd.estela.backend.dto.game.ReponseGamesDetailsDTO;
import dev.smd.estela.backend.model.Game;
import dev.smd.estela.backend.model.Media;
import dev.smd.estela.backend.model.Sale;
import dev.smd.estela.backend.model.MediaType;
import dev.smd.estela.backend.model.SaleGame;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameService {

    private static final GameDAO gameDao = new GameDAO();
    private static final CategoriesGamesDAO categoryGameDao = new CategoriesGamesDAO();
    private static final MediaDAO mediaDao = new MediaDAO();
    private static final SaleService saleService = new SaleService();

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

    public boolean saveGame(BigDecimal price, String name, String characteristics, String description, String hardDriveSpace, String graphicsCard, String memory, String operatingSystem, String processor, Part coverPart, Part iconPart, List<Part> listOfScreenshots, List<Long> categoryIds) {
        Game newGame = new Game(null, price, name, characteristics, null, description, hardDriveSpace, graphicsCard, memory, operatingSystem, processor, null);
        boolean isSucess = gameDao.save(newGame);

        if (isSucess) {
            Game gameSaved = gameDao.getByName(name);
            Long newGameId = gameSaved.getGameId();

            if (categoryIds != null) {
                for (Long id : categoryIds) {
                    try {
                        categoryGameDao.linkGameToCategory(newGameId, id);
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido: " + id);
                    }
                }
            }

            String converGame = "cover_game_" + gameSaved.getGameId();
            Media converGameMedia = new Media(converGame + getExtension(coverPart), MediaType.COVER, gameSaved.getGameId());
            isSucess = mediaDao.save(converGameMedia);
            saveFile(coverPart, converGame);

            String IconGame = "icon_game_" + gameSaved.getGameId();
            Media iconGameMedia = new Media(IconGame + getExtension(iconPart), MediaType.ICON, gameSaved.getGameId());
            isSucess = mediaDao.save(iconGameMedia);
            saveFile(iconPart, IconGame);

            for (int i = 0; i < listOfScreenshots.size(); i++) {
                Part filePart = listOfScreenshots.get(i);
                String fileName = "screenshot_" + (i) + "_" + gameSaved.getGameId();
                Media mediaGameMedia = new Media(fileName + getExtension(filePart), MediaType.SCREENSHOT, gameSaved.getGameId());
                isSucess = mediaDao.save(mediaGameMedia);
                saveFile(filePart, fileName);
            }
        }

        return isSucess;
    }

    private void saveFile(Part filePart, String fileName) {
        try {
            String fileExtension = getExtension(filePart);
            String newFileName = fileName + fileExtension;
            String uploadPath = PATH_FILES_GAMES;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            File finalFile = new File(uploadDir, newFileName);
            String filePath = finalFile.getAbsolutePath();
            filePart.write(filePath);
        } catch (IOException e) {
            System.out.println(e.getCause());
        }
    }

    private void deleteFile(String fileNameWithExtension) {
        try {
            if (fileNameWithExtension == null || fileNameWithExtension.isEmpty()) {
                System.out.println("Nome do arquivo inválido para exclusão.");
                return;
            }

            String uploadPath = PATH_FILES_GAMES;
            File file = new File(uploadPath, fileNameWithExtension);

            if (file.exists()) {
                file.delete();
            } else {
                System.out.println("Arquivo não encontrado para exclusão: " + file.getAbsolutePath());
            }

        } catch (Exception e) {
            System.out.println("Erro ao tentar deletar arquivo: " + e.getMessage());
        }
    }

    public boolean updateGame(Long gameId, BigDecimal price, String name, String characteristics,
            String description, String hardDriveSpace, String graphicsCard,
            String memory, String operatingSystem, String processor,
            Part coverPart, Part iconPart, List<Part> listOfScreenshots,
            List<Long> categoryIds) {

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
                if (!categoryIds.isEmpty()) {
                    gameDao.clearCategories(gameId);
                    for (Long id : categoryIds) {
                        try {
                            categoryGameDao.linkGameToCategory(gameId, id);
                        } catch (NumberFormatException e) {
                            System.out.println("ID inválido: " + id);
                        }
                    }
                }

                if (coverPart != null && coverPart.getSize() > 0) {
                    mediaDao.deleteByGameIdAndType(gameId, MediaType.COVER);

                    String ext = getExtension(coverPart);
                    String fileName = "cover_game_" + gameId + ext;

                    mediaDao.save(new Media(fileName, MediaType.COVER, gameId));
                    saveFile(coverPart, "cover_game_" + gameId);
                }

                if (iconPart != null && iconPart.getSize() > 0) {
                    mediaDao.deleteByGameIdAndType(gameId, MediaType.ICON);
                    String ext = getExtension(iconPart);
                    String fileName = "icon_game_" + gameId + ext;
                    mediaDao.save(new Media(fileName, MediaType.ICON, gameId));
                    saveFile(iconPart, "icon_game_" + gameId);
                }

                boolean hasGalleryUpdate = listOfScreenshots.stream().anyMatch(p -> p != null && p.getSize() > 0);

                if (hasGalleryUpdate) {
                    mediaDao.deleteByGameIdAndType(gameId, MediaType.SCREENSHOT);

                    for (int i = 0; i < listOfScreenshots.size(); i++) {
                        Part filePart = listOfScreenshots.get(i);
                        String extensionScreenshot = filePart.getSubmittedFileName().substring(filePart.getSubmittedFileName().lastIndexOf("."));;
                        String fileName = "screenshot_" + (i) + "_" + gameId;
                        Media mediaGameMedia = new Media(fileName + extensionScreenshot, MediaType.SCREENSHOT, gameId);
                        mediaDao.save(mediaGameMedia);
                        saveFile(filePart, fileName);
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
        ReponseGameDetailsDTO game = gameDao.getGameDetailsById(gameId);
        deleteFile(game.getUrlCover());
        mediaDao.deleteByGameIdAndType(gameId, MediaType.COVER);
        deleteFile(game.getUrlIcon());
        mediaDao.deleteByGameIdAndType(gameId, MediaType.ICON);

        for (String urlScreenshot : game.getUrlsScreenshots()) {
            deleteFile(urlScreenshot);
            mediaDao.deleteByGameIdAndType(gameId, MediaType.SCREENSHOT);
        }

        return gameDao.deleteById(gameId);
    }

    public ReponseGameDetailsDTO getDeitalsGameById(Long gameId) {
        return gameDao.getGameDetailsById(gameId);
    }

    public ArrayList<ReponseGamePurchasedDTO> getGamesPurchasedByUserId(long userId) {
        ArrayList<ReponseGamePurchasedDTO> listOfGames = new ArrayList<>();
        ArrayList<Sale> listSales = saleService.getSalesByUserId(userId);
        for (int i = 0; i < listSales.size(); i++) {
            ArrayList<SaleGame> listSalesGames = saleService.getSalesGamesBySaleId(listSales.get(i).getSaleId());
            for (int j = 0; j < listSalesGames.size(); j++) {
                SaleGame current = listSalesGames.get(j);
                Game currentGame = getByIdWithIconAndCover(current.getGameId());
                ReponseGamePurchasedDTO reponseGamePurchasedDTO = new ReponseGamePurchasedDTO(currentGame.getGameId(), currentGame.getName(), currentGame.getPrice(), currentGame.getUrlCover(), currentGame.getUrlIcon(), listSales.get(i).getDataSale());
                listOfGames.add(reponseGamePurchasedDTO);
            }
        }
        return listOfGames;
    }

    public Game getByIdWithIconAndCover(Long gameId) {
        return gameDao.getByIdWithIconAndCover(gameId);
    }
}
