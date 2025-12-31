package dev.smd.estela.backend.dto.game;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    private List<Long> categoryIds;
    private List<String> urlsScreenshots;

    public ReponseGameDetailsDTO(Long gameId, String name, BigDecimal price, String description, String urlIcon, String urlCover, String characteristics, LocalDateTime createdAt, String hardDriveSpace, String graphicsCard, String memory, String operatingSystem, String processor, List<Long> categoryIds, List<String> urlsMedia) {
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
        this.categoryIds = categoryIds;
        this.urlsScreenshots = urlsMedia;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlIcon() {
        return urlIcon;
    }

    public void setUrlIcon(String urlIcon) {
        this.urlIcon = urlIcon;
    }

    public String getUrlCover() {
        return urlCover;
    }

    public void setUrlCover(String urlCover) {
        this.urlCover = urlCover;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getHardDriveSpace() {
        return hardDriveSpace;
    }

    public void setHardDriveSpace(String hardDriveSpace) {
        this.hardDriveSpace = hardDriveSpace;
    }

    public String getGraphicsCard() {
        return graphicsCard;
    }

    public void setGraphicsCard(String graphicsCard) {
        this.graphicsCard = graphicsCard;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public List<String> getUrlsScreenshots() {
        return urlsScreenshots;
    }

    public void setUrlsScreenshots(List<String> urlsScreenshots) {
        this.urlsScreenshots = urlsScreenshots;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    @Override
    public String toString() {
        return "ReponseGameDetailsDTO{" + "gameId=" + gameId + ", name=" + name + ", price=" + price + ", description=" + description + ", urlIcon=" + urlIcon + ", urlCover=" + urlCover + ", characteristics=" + characteristics + ", createdAt=" + createdAt + ", hardDriveSpace=" + hardDriveSpace + ", graphicsCard=" + graphicsCard + ", memory=" + memory + ", operatingSystem=" + operatingSystem + ", processor=" + processor + ", categoryIds=" + categoryIds + ", urlsScreenshots=" + urlsScreenshots + '}';
    }

}
