package dev.smd.estela.backend.servlet.sale;

import dev.smd.estela.backend.service.SaleService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "SaleServlet", urlPatterns = {"/api/adm/sale"})
public class SaleServlet extends HttpServlet {

    private final SaleService saleService = new SaleService();

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
}
