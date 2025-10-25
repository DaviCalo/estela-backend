/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.smd.estela.backend.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class SaleGame implements Serializable {

        private Long saleId;
        private Long gameId;
        private BigDecimal price;

        public SaleGame() {
        }

        public SaleGame(Long saleId, Long gameId, BigDecimal price) {
            this.saleId = saleId;
            this.gameId = gameId;
            this.price = price;
        }

        public Long getSaleId() {
            return saleId;
        }

        public void setSaleId(Long saleId) {
            this.saleId = saleId;
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
}