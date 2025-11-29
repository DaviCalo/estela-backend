package dev.smd.estela.backend.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.smd.estela.backend.service.GameService;
import dev.smd.estela.backend.dto.ReponseGamesDTO;
import dev.smd.estela.backend.utils.LocalDateTimeAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "listofgames", urlPatterns = {"/api/games"})
public class ListOfGamesServlet extends HttpServlet {

        private final GameService gameService = new GameService();
        Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .create();

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();

                ArrayList<ReponseGamesDTO> games = gameService.listAllGames();

                Map<String, ArrayList<ReponseGamesDTO>> responseMap = new HashMap<>();
                responseMap.put("games", games);
                out.print(gson.toJson(responseMap));
        }
}
