package dev.smd.estela.backend.dto.sale;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import dev.smd.estela.backend.dto.game.GameReportDTO;

public class SalesReportDTO {

    private Long userId;
    private String userName;
    private Long saleId;
    private LocalDateTime dataSale;
    private BigDecimal totalPrice;
    private final ArrayList<GameReportDTO> listOfGames;

    public SalesReportDTO() {
        this.listOfGames = new ArrayList();

    }

    public SalesReportDTO(Long userId, String userName, Long saleId, LocalDateTime dataSale, BigDecimal totalPrice) {
        this.listOfGames = new ArrayList();
        this.userId = userId;
        this.userName = userName;
        this.saleId = saleId;
        this.dataSale = dataSale;
        this.totalPrice = totalPrice;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public LocalDateTime getDataSale() {
        return dataSale;
    }

    public void setDataSale(LocalDateTime dataSale) {
        this.dataSale = dataSale;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<GameReportDTO> getListOfGames() {
        return listOfGames;
    }

    public void addListOfGames(GameReportDTO gameReportDTO) {
        this.listOfGames.add(gameReportDTO);
    }
}
