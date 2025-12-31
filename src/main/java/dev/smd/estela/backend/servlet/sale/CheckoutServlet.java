package dev.smd.estela.backend.servlet.sale;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import dev.smd.estela.backend.service.SaleService;
import java.util.ArrayList;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/api/checkout"})
public class CheckoutServlet extends HttpServlet {

    private final Gson gson = new Gson();
    private final SaleService saleService = new SaleService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            CheckoutDTO checkoutData = gson.fromJson(request.getReader(), CheckoutDTO.class);
            if (checkoutData == null || checkoutData.userId == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Id do usuário é obrigatório\"}");
                return;
            }
            if (checkoutData.gamesId == null || checkoutData.gamesId.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Id dos jogos é obrigatório\"}");
                return;
            }
            saleService.checkoutGames(checkoutData.userId, checkoutData.gamesId);
            out.print("{\"status\": \"success\"}");
        } catch (JsonIOException | JsonSyntaxException | IOException e) {
            System.out.println(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Ocorreu um erro inesperado\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Long saleId = Long.valueOf(request.getParameter("userId"));
            if (saleId == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Id do usuário é obrigatório\"}");
                return;
            }
            saleService.deleteSale(saleId);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Ocorreu um erro inesperado\"}");
        }
    }

    private static class CheckoutDTO {

        Long userId;
        ArrayList<Long> gamesId;
    }
}
