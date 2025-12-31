package dev.smd.estela.backend.servlet.category;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.smd.estela.backend.dto.category.ReponseCategoryDTO;
import dev.smd.estela.backend.service.CategoryService;
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

@WebServlet(name = "ListOfCategoryServlet", urlPatterns = {"/api/categories"})
public class ListOfCategoryServlet extends HttpServlet {

    private final CategoryService categoryService = new CategoryService();

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        ArrayList<ReponseCategoryDTO> categories = categoryService.listAllCategory();
        Map<String, ArrayList<ReponseCategoryDTO>> responseMap = new HashMap<>();
        responseMap.put("categories", categories);
        out.print(gson.toJson(responseMap));
    }
}
