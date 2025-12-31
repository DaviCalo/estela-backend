package dev.smd.estela.backend.service;

import dev.smd.estela.backend.dao.SaleDAO;
import dev.smd.estela.backend.dao.SalesGamesDAO;
import dev.smd.estela.backend.dto.game.GameReportDTO;
import dev.smd.estela.backend.dto.game.ReponseGameDetailsDTO;
import dev.smd.estela.backend.dto.sale.SalesReportDTO;
import dev.smd.estela.backend.model.SaleGame;
import dev.smd.estela.backend.model.Sale;
import java.util.ArrayList;
import java.util.List;

public class SaleService {

    private static final GameService gameService = new GameService();
    private static final SaleDAO saleDAO = new SaleDAO();
    private static final SalesGamesDAO salesGamesDAO = new SalesGamesDAO();

    public void checkoutGames(Long userId, ArrayList<Long> gamesId) {
        try {
            Sale newSale = new Sale(userId);
            newSale = saleDAO.save(newSale);
            for (Long gameId : gamesId) {
                ReponseGameDetailsDTO game = gameService.getDeitalsGameById(gameId);
                SaleGame newGameSale = new SaleGame(newSale.getSaleId(), gameId, game.getPrice());
                salesGamesDAO.save(newGameSale);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    ArrayList<Sale> getSalesByUserId(long userId) {
        return saleDAO.findByUserId(userId);
    }

    ArrayList<SaleGame> getSalesGamesBySaleId(long saleId) {
        return salesGamesDAO.findBySaleId(saleId);
    }

    public void deleteSale(Long saleId) {
        saleDAO.deleteById(saleId);
    }

    public List<SalesReportDTO> getSalesReport(String nameClientFilter, String startDateStr, String endDateStr) {
        List<SalesReportDTO> listOfSalesReport = new ArrayList();
        listOfSalesReport = saleDAO.getSalesReport(nameClientFilter, startDateStr, endDateStr);
        for (SalesReportDTO saleReport : listOfSalesReport) {
            ArrayList<SaleGame> listOfSaleGame = getSalesGamesBySaleId(saleReport.getSaleId());
            for (SaleGame saleGame : listOfSaleGame) {
                GameReportDTO gameReportDTO = new GameReportDTO();
                gameReportDTO.setGameId(saleGame.getGameId());
                gameReportDTO.setPrice(saleGame.getPrice());
                gameReportDTO.setName(gameService.getByIdWithIconAndCover(saleGame.getGameId()).getName());
                saleReport.addListOfGames(gameReportDTO);
            }
        }
        return listOfSalesReport;
    }
}
