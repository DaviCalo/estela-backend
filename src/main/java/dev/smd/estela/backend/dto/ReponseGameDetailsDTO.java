package dev.smd.estela.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List; // Import necessário

public class ReponseGameDetailsDTO {
    private Long gameId;
    private String name;
    private BigDecimal price;
    private String description;
    private String urlIcon;
    private String urlCover;
    private String characteristics;
    private LocalDateTime createdAt;
    private String hardDriveSpace;
    private String graphicsCard;
    private String memory;
    private String operatingSystem;
    private String processor;
    private List<Long> categoryIds; // Novo campo

    public ReponseGameDetailsDTO(Long gameId, String name, BigDecimal price, String description, String urlIcon, String urlCover, String characteristics, LocalDateTime createdAt, String hardDriveSpace, String graphicsCard, String memory, String operatingSystem, String processor, List<Long> categoryIds) {
        this.gameId = gameId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.urlIcon = urlIcon;
        this.urlCover = urlCover;
        this.characteristics = characteristics;
        this.createdAt = createdAt;
        this.hardDriveSpace = hardDriveSpace;
        this.graphicsCard = graphicsCard;
        this.memory = memory;
        this.operatingSystem = operatingSystem;
        this.processor = processor;
        this.categoryIds = categoryIds; // Atribuição
    }

    // Getters e Setters (se necessário) podem ser adicionados aqui
    public List<Long> getCategoryIds() {
        return categoryIds;
    }
}