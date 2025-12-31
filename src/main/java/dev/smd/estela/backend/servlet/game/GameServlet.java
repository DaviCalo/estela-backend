package dev.smd.estela.backend.servlet.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.smd.estela.backend.service.GameService;
import dev.smd.estela.backend.dto.game.ReponseGameDetailsDTO;
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
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

@WebServlet(name = "GameServlet", urlPatterns = {"/api/game"})
@MultipartConfig(
        fileSizeThreshold = 1048576,
        maxFileSize = 524288000,
        maxRequestSize = 576716800
)
public class GameServlet extends HttpServlet {

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

        ArrayList<Part> listOfScreenshots = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            Part photoPart = request.getPart("screenshots_" + i);
            if (photoPart == null
                    || photoPart.getSubmittedFileName() == null
                    || photoPart.getSubmittedFileName().trim().isEmpty()
                    || photoPart.getSize() == 0) {
                break;
            }
            listOfScreenshots.add(photoPart);
        }

        List<Long> categoryIds;

        if (categoryIdsParam != null && !categoryIdsParam.isEmpty()) {
            categoryIds = Arrays.stream(categoryIdsParam
                    .replace("[", "")
                    .replace("]", "")
                    .split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        } else {
            categoryIds = Collections.emptyList();
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
                listOfScreenshots,
                categoryIds
        );

        response.setContentType("application/json");
        response.getWriter().print("{\"status\": \"sucess\"}");
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
            String gameIdParam = request.getParameter("gameId");

            if (gameIdParam == null || gameIdParam.trim().isEmpty() || gameIdParam.equals("null")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"O ID do jogo é obrigatório para atualização.\"}");
                return;
            }

            Long gameId = Long.valueOf(gameIdParam);
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

            List<Long> categoryIds;

            if (categoryIdsParam != null && !categoryIdsParam.isEmpty()) {
                categoryIds = Arrays.stream(categoryIdsParam
                        .replace("[", "")
                        .replace("]", "")
                        .split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Long::valueOf)
                        .collect(Collectors.toList());
            } else {
                categoryIds = Collections.emptyList();
            }

            ArrayList<Part> listOfScreenshots = new ArrayList<>();

            for (int i = 0; i < 6; i++) {
                Part photoPart = request.getPart("screenshots_" + i);
                if (photoPart == null
                        || photoPart.getSubmittedFileName() == null
                        || photoPart.getSubmittedFileName().trim().isEmpty()
                        || photoPart.getSize() == 0) {
                    break;
                }
                listOfScreenshots.add(photoPart);
            }

            boolean success = gameService.updateGame(
                    gameId,
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
                    listOfScreenshots,
                    categoryIds
            );

            if (success) {
                out.print("{\"status\": \"success\", \"message\": \"Jogo atualizado com sucesso\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"Erro ao atualizar o jogo no banco de dados\"}");
            }

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID do jogo inválido\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Erro interno: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
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

    public static BigDecimal stringToBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        String valueFormatter = value.trim();

        if (valueFormatter.contains(",")) {
            valueFormatter = valueFormatter.replace(".", "");
            valueFormatter = valueFormatter.replace(",", ".");
        }
        try {
            return new BigDecimal(valueFormatter);
        } catch (NumberFormatException e) {
            throw e;
        }
    }
}
