/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.smd.estela.backend.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Game implements Serializable {

        private Long gameId;
        private BigDecimal price;
        private String name;
        private String urlCover;
        private String characteristics;
        private String description;
        private String hardDriveSpace;
        private String graphicsCard;
        private String memory;
        private String operatingSystem;
        private String processor;
        private LocalDateTime createdAt;

        public Game() {
        }

        public Game(Long gameId, BigDecimal price, String name, String characteristics, String urlCover, String description, String hardDriveSpace, String graphicsCard, String memory, String operatingSystem, String processor, LocalDateTime createdAt) {
            this.gameId = gameId;
            this.price = price;
            this.name = name;
            this.characteristics = characteristics;
            this.description = description;
            this.hardDriveSpace = hardDriveSpace;
            this.graphicsCard = graphicsCard;
            this.memory = memory;
            this.operatingSystem = operatingSystem;
            this.processor = processor;
            this.createdAt = createdAt;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public LocalDateTime getCreatedAt() {
                return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
                this.createdAt = createdAt;
        }

        @Override
        public String toString() {
                return "Game{" + "gameId=" + gameId + ", price=" + price + ", name=" + name + ", urlCover=" + urlCover + ", characteristics=" + characteristics + ", description=" + description + ", hardDriveSpace=" + hardDriveSpace + ", graphicsCard=" + graphicsCard + ", memory=" + memory + ", operatingSystem=" + operatingSystem + ", processor=" + processor + ", createdAt=" + createdAt + '}';
        }
        
}
