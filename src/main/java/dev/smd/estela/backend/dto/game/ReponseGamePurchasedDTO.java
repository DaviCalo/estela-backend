package dev.smd.estela.backend.dto.game;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ReponseGamePurchasedDTO {

    private Long gameId;
    private String name;
    private BigDecimal price;
    private String coverUrl;
    private String iconUrl;
    private Timestamp dataSale;

    public ReponseGamePurchasedDTO(Long gameId, String name, BigDecimal price, String coverUrl, String iconUrl, Timestamp dataSale) {
        this.gameId = gameId;
        this.name = name;
        this.price = price;
        this.coverUrl = coverUrl;
        this.iconUrl = iconUrl;
        this.dataSale = dataSale;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Timestamp getDataSale() {
        return dataSale;
    }

    public void setDataSale(Timestamp dataSale) {
        this.dataSale = dataSale;
    }

}
