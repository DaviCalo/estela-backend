package dev.smd.estela.backend.dto.game;

import java.math.BigDecimal;

public class GameReportDTO {

    private Long gameId;
    private BigDecimal price;
    private String name;

    public GameReportDTO() {}

    public GameReportDTO(Long gameId, BigDecimal price, String name) {
        this.gameId = gameId;
        this.price = price;
        this.name = name;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
}
