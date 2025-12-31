package dev.smd.estela.backend.model;

import java.io.Serializable;

public class Wishlist implements Serializable {

        private Long userId;
        private Long gameId;

        public Wishlist() {
        }

        public Wishlist(Long userId, Long gameId) {
            this.userId = userId;
            this.gameId = gameId;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getGameId() {
            return gameId;
        }

        public void setGameId(Long gameId) {
            this.gameId = gameId;
        }
}