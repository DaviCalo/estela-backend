package dev.smd.estela.backend.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.smd.estela.backend.service.GameService;
import dev.smd.estela.backend.dto.ReponseGameDetailsDTO;
import dev.smd.estela.backend.dto.ReponseGamesDTO;
import dev.smd.estela.backend.utils.LocalDateTimeAdapter;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "GameServlet", urlPatterns = {"/api/game"})
@MultipartConfig(
        fileSizeThreshold = 1048576,
        maxFileSize = 524288000,
        maxRequestSize = 576716800
)
public class GameServlet extends HttpServlet {

    private static final int BUFFER_SIZE = 4096;
    private final GameService gameService = new GameService();
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String nameParam = request.getParameter("name");
        String descriptionParam = request.getParameter("description");
        BigDecimal priceParam = stringToBigDecimal(request.getParameter("price"));
        String categoryIdsParam = request.getParameter("categoryIds");
        String characteristicsParam = request.getParameter("characteristics");

        String hardDriveSpaceParam = request.getParameter("hardDriveSpace");
        String graphicsCardParam = request.getParameter("graphicsCard");
        String memoryParam = request.getParameter("memory");
        String operatingSystemParam = request.getParameter("operatingSystem");
        String processorParam = request.getParameter("processor");
        Part coverPart = request.getPart("coverGame");
        Part iconPart = request.getPart("iconImage");

        ArrayList<Part> listOfMidias = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
            Part photoPart = request.getPart("midiaGame" + i);

            if (photoPart != null && photoPart.getSize() > 0) {
                String fileName = photoPart.getSubmittedFileName();
                System.out.println("Recebida foto: " + fileName);
                listOfMidias.add(photoPart);
            } else {
                System.out.println("Foto " + i + " não enviada.");
            }
        }

        gameService.saveGame(
                priceParam,
                nameParam,
                characteristicsParam,
                descriptionParam,
                hardDriveSpaceParam,
                graphicsCardParam,
                memoryParam,
                operatingSystemParam,
                processorParam,
                coverPart,
                iconPart,
                listOfMidias
        );

        response.setContentType("application/json");
        response.getWriter().print("{\"status\": \"sucess\"}");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String gameIdParam = request.getParameter("gameId");

        if (gameIdParam == null || gameIdParam.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Id do jogo é obrigatório\"}");
            return;
        }

        try {
            Long gameId = Long.valueOf(gameIdParam);

            boolean isSuccess = gameService.deleteGame(gameId);

            if (isSuccess) {
                out.print("{\"status\": \"success\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Jogo não encontrado ou não pôde ser deletado\"}");
            }

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"O ID do jogo deve ser um número válido\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Erro interno no servidor: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String gameIdParam = request.getParameter("gameId");

        if (gameIdParam == null || gameIdParam.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Id do jogo é obrigatório\"}");
            return;
        }

        try {
            Long gameId = Long.valueOf(gameIdParam);
            ReponseGameDetailsDTO reponseGamesDetailsDTO = gameService.getDeitalsGameById(gameId);
            if (reponseGamesDetailsDTO == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Jogo não encontrado\"}");
                return;
            }
            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("game", reponseGamesDetailsDTO);
            out.print(gson.toJson(jsonResponse));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"O ID do jogo deve ser um número válido\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Erro interno no servidor: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Coleta de parâmetros comuns
            String nameParam = request.getParameter("name");
            String descriptionParam = request.getParameter("description");
            BigDecimal priceParam = stringToBigDecimal(request.getParameter("price"));
            String categoryIdsParam = request.getParameter("categoryIds");
            String characteristicsParam = request.getParameter("characteristics");
            String hardDriveSpaceParam = request.getParameter("hardDriveSpace");
            String graphicsCardParam = request.getParameter("graphicsCard");
            String memoryParam = request.getParameter("memory");
            String operatingSystemParam = request.getParameter("operatingSystem");
            String processorParam = request.getParameter("processor");

            Part coverPart = request.getPart("coverGame");
            Part iconPart = request.getPart("iconImage");

            ArrayList<Part> listOfMidias = new ArrayList<>();
            for (int i = 1; i <= 6; i++) {
                Part photoPart = request.getPart("midiaGame" + i);
                listOfMidias.add(photoPart); // Adiciona mesmo se for null, o Service filtra
            }

            // VERIFICAÇÃO CRUCIAL: É Update ou Save?
            String gameIdParam = request.getParameter("gameId");

            boolean success;

            if (gameIdParam != null && !gameIdParam.trim().isEmpty() && !gameIdParam.equals("null")) {
                // === MODO UPDATE ===
                Long gameId = Long.valueOf(gameIdParam);
                success = gameService.updateGame(
                        gameId, priceParam, nameParam, characteristicsParam, descriptionParam,
                        hardDriveSpaceParam, graphicsCardParam, memoryParam, operatingSystemParam, processorParam,
                        coverPart, iconPart, listOfMidias, categoryIdsParam
                );
            } else {
                // === MODO CREATE (Seu código antigo adaptado para aceitar categories se precisar) ===
                // Nota: Se quiser salvar categorias no Create também, precisa ajustar o saveGame original.
                success = gameService.saveGame(
                        priceParam, nameParam, characteristicsParam, descriptionParam,
                        hardDriveSpaceParam, graphicsCardParam, memoryParam, operatingSystemParam, processorParam,
                        coverPart, iconPart, listOfMidias
                // Adicione categoryIdsParam aqui se atualizar o saveGame também
                );
            }

            if (success) {
                out.print("{\"status\": \"success\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"Erro ao processar o jogo\"}");
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Erro interno: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }

    public static BigDecimal stringToBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        String valueFormatter = value.trim();
        valueFormatter = valueFormatter.replace(".", "");
        valueFormatter = valueFormatter.replace(",", ".");

        try {
            return new BigDecimal(valueFormatter);
        } catch (NumberFormatException e) {
            System.err.println("Erro de formato de número para a string: " + value);
            throw new IllegalArgumentException("String inválida para conversão.", e);
        }
    }
}
