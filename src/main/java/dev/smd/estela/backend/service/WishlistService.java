package dev.smd.estela.backend.service;

import dev.smd.estela.backend.dao.WishlistDAO;
import dev.smd.estela.backend.model.Wishlist;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WishlistService {

    private final WishlistDAO wishlistDAO = new WishlistDAO();

    public boolean saveWishlist(Long userId, Long gameId) {
        boolean isSucess = true;
        Wishlist wishList = new Wishlist(userId, gameId);
        wishlistDAO.save(wishList);
        return isSucess;
    }

    public boolean deleteWishlist(Long userId, Long gameId) {
        boolean isSucess = true;
        Wishlist wishList = new Wishlist(userId, gameId);
        wishlistDAO.delete(wishList);
        return isSucess;
    }

    public ArrayList<Long> getWishlistByUserId(Long userId) {
        ArrayList<Long> listGameWishlist = new ArrayList();
        List<Wishlist> listWishlist = wishlistDAO.findByUserId(userId);
        listGameWishlist = listWishlist.stream().map(wishlist -> wishlist.getGameId()).collect(Collectors.toCollection(ArrayList::new));
        return listGameWishlist;
    }
}
