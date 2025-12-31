package dev.smd.estela.backend.servlet.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.smd.estela.backend.dto.game.ReponseGamePurchasedDTO;
import dev.smd.estela.backend.service.GameService;
import dev.smd.estela.backend.utils.LocalDateTimeAdapter;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "UserLibraryServlet", urlPatterns = {"/api/my-games"})
public class UserLibraryServlet extends HttpServlet {

    GameService gameService = new GameService();
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String userIdStr = request.getParameter("userId");

            if (userIdStr == null || userIdStr.isEmpty()) {
                response.setStatus(400);
                return;
            }
            long userId = Long.parseLong(userIdStr);
            ArrayList<ReponseGamePurchasedDTO> userGames = gameService.getGamesPurchasedByUserId(userId);
            Map<String, ArrayList<ReponseGamePurchasedDTO>> responseMap = new HashMap<>();
            responseMap.put("games", userGames);
            out.print(gson.toJson(responseMap));
            out.flush();
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Erro ao buscar jogos\"}");
        }
    }
}
