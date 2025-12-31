package dev.smd.estela.backend.dto.game;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReponseGamesDTO {

    private Long gameId;
    private String name;
    private BigDecimal price;
    private String coverUrl;
    private LocalDateTime createdAt;

    public ReponseGamesDTO(Long gameId, String name, BigDecimal price, String coverUrl, LocalDateTime createdAt) {
        this.gameId = gameId;
        this.name = name;
        this.price = price;
        this.coverUrl = coverUrl;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ReponseGamesDTO{" + "gameId=" + gameId + ", name=" + name + ", price=" + price + ", coverUrl=" + coverUrl + ", createdAt=" + createdAt + '}';
    }
}
