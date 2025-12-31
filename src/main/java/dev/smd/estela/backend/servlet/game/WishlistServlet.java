package dev.smd.estela.backend.servlet.game;

import dev.smd.estela.backend.service.WishlistService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dev.smd.estela.backend.dto.game.WishlistInput;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "WishListServlet", urlPatterns = {"/api/wishlist"})
public class WishlistServlet extends HttpServlet {

    private final WishlistService wishlistService = new WishlistService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Configurações de Encoding e Tipo (Entrada e Saída)
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            WishlistInput inputData = gson.fromJson(request.getReader(), WishlistInput.class);

            if (inputData == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("error", "O corpo da requisição (JSON) está vazio ou inválido.");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            if (inputData.userId == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("error", "O campo 'userId' é obrigatório.");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            if (inputData.gameId == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("error", "O campo 'gameId' é obrigatório.");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            boolean isSuccess = wishlistService.saveWishlist(inputData.userId, inputData.gameId);

            if (isSuccess) {
                response.setStatus(HttpServletResponse.SC_OK);
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Jogo adicionado à lista de desejos.");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("success", false);
                jsonResponse.put("error", "Não foi possível salvar os dados.");
            }

        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("error", "JSON inválido: " + e.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("error", "Erro interno: " + e.getMessage());
        }

        if (!jsonResponse.isEmpty() && !response.isCommitted()) {
            out.print(gson.toJson(jsonResponse));
        }
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String userIdString = request.getParameter("userId");
        String gameIdString = request.getParameter("gameId");

        if (userIdString == null || userIdString.trim().isEmpty() || userIdString.equals("null")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"O ID do usúario é obrigatório.\"}");
            return;
        }

        if (gameIdString == null || gameIdString.trim().isEmpty() || gameIdString.equals("null")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"O ID do jogo é obrigatório.\"}");
            return;
        }

        boolean isSucess = wishlistService.deleteWishlist(Long.valueOf(userIdString), Long.valueOf(gameIdString));

        if (isSucess) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String userIdString = request.getParameter("userId");

        if (userIdString == null || userIdString.trim().isEmpty() || userIdString.equals("null")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"O ID do usúario é obrigatório.\"}");
            return;
        }

        ArrayList<Long> listGameWishlist = wishlistService.getWishlistByUserId(Long.valueOf(userIdString));

        Map<String, ArrayList<Long>> responseMap = new HashMap<>();
        responseMap.put("games", listGameWishlist);
        out.print(gson.toJson(responseMap));
    }

}
