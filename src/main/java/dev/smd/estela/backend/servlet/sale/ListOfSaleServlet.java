package dev.smd.estela.backend.servlet.sale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.smd.estela.backend.dto.sale.SalesReportDTO;
import dev.smd.estela.backend.service.SaleService;
import dev.smd.estela.backend.utils.LocalDateTimeAdapter;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "ListOfSaleServlet", urlPatterns = {"/api/adm/sales"})
public class ListOfSaleServlet extends HttpServlet {

    private final SaleService saleService = new SaleService();
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String nameClientFilter = request.getParameter("name");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        try {
            List<SalesReportDTO> relatorio = saleService.getSalesReport(nameClientFilter, startDateStr, endDateStr);

            String json = gson.toJson(relatorio);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Ocorreu um erro inesperado\"}");
        }
    }
}
