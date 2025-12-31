package dev.smd.estela.backend.dto.game;

public class WishlistInput {
    public Long userId;
    public Long gameId;

    public WishlistInput(Long userId, Long gameId) {
        this.userId = userId;
        this.gameId = gameId;
    }
}
