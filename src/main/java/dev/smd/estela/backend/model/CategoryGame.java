package dev.smd.estela.backend.model;

import java.io.Serializable;

public class CategoryGame implements Serializable {

        private Long categoryId;
        private Long gameId;

        public CategoryGame() {
        }

        public CategoryGame(Long categoryId, Long gameId) {
            this.categoryId = categoryId;
            this.gameId = gameId;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }

        public Long getGameId() {
            return gameId;
        }

        public void setGameId(Long gameId) {
            this.gameId = gameId;
        }
}