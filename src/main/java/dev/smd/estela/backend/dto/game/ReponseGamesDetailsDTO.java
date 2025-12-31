package dev.smd.estela.backend.dto.game;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReponseGamesDetailsDTO {

    private Long gameId;
    private String name;
    private BigDecimal price;
    private String description;
    private LocalDateTime createdAt;
    private int sold;

    public ReponseGamesDetailsDTO(Long gameId, String name, BigDecimal price, String description, LocalDateTime createdAt, int sold) {
        this.gameId = gameId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.createdAt = createdAt;
        this.sold = sold;
    }

}
