/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.smd.estela.backend.model;

import java.io.Serializable;
 import java.sql.Timestamp;

public class Sale implements Serializable {
        private Long saleId;
        private Timestamp dataSale;
        private Long userId;

        public Sale() {
        }

        public Sale(Long saleId, Timestamp dataSale, Long userId) {
            this.saleId = saleId;
            this.dataSale = dataSale;
            this.userId = userId;
        }

        public Long getSaleId() {
            return saleId;
        }

        public void setSaleId(Long saleId) {
            this.saleId = saleId;
        }

        public Timestamp getDataSale() {
            return dataSale;
        }

        public void setDataSale(Timestamp dataSale) {
            this.dataSale = dataSale;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }
}