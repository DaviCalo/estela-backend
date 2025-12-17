package dev.smd.estela.backend.servlet;

import dev.smd.estela.backend.service.GameService;
import java.io.IOException;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;

@WebServlet(name = "GameServlet", urlPatterns = {"/api/game"})
@MultipartConfig(
          fileSizeThreshold = 1048576,
          maxFileSize = 524288000,
          maxRequestSize = 576716800
)
public class GameServlet extends HttpServlet {

        private static final int BUFFER_SIZE = 4096;
        private final GameService gameService = new GameService();

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
