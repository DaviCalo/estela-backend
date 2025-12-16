package dev.smd.estela.backend.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import dev.smd.estela.backend.dto.UpdateUserDTO;
import dev.smd.estela.backend.model.User;
import dev.smd.estela.backend.service.UserService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "UserServlet", urlPatterns = {"/api/user"})
public class UserServlet extends HttpServlet {

        private final Gson gson = new Gson();
        private final UserService userService = new UserService();

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                String userIdString = request.getParameter("userid");

                if (userIdString == null || userIdString.trim().isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\": \"Id do usuario é obrigatório\"}");
                        return;
                }

                Long userIdParam;
                try {
                        userIdParam = Long.valueOf(userIdString);
                } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\": \"Id do usuario inválido\"}");
                        return;
                }

                User user = userService.getUserById(userIdParam);

                if (user == null) {
                        out.print("{\"status\": \"error\", \"message\": \"Usuário com ID " + userIdParam + " não encontrado.\"}");
                } else {
                        response.setStatus(HttpServletResponse.SC_OK);
                        java.util.Map<String, Object> responseMap = new java.util.HashMap<>();
                        responseMap.put("status", "success");
                        responseMap.put("user", user);
                        String userJson = gson.toJson(responseMap);
                        out.print(userJson);
                }

                out.flush();
        }

        @Override
        protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();

                UpdateUserDTO updateUserDTO;
                try {
                        updateUserDTO = gson.fromJson(request.getReader(), UpdateUserDTO.class);
                        if (updateUserDTO == null) {
                                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                out.print("{\"error\": \"Corpo da requisição ausente ou JSON inválido.\"}");
                                return;
                        }
                } catch (JsonIOException | JsonSyntaxException | IOException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\": \"JSON mal formatado: " + e.getMessage() + "\"}");
                        return;
                }

                if (updateUserDTO.getNickname() == null
                          || updateUserDTO.getPassword() == null
                          || updateUserDTO.getEmail() == null
                          || updateUserDTO.getName() == null) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\": \"Todos os campos são obrigatorios\"}");
                        return;
                }

                Boolean isSucess = userService.updateUser(updateUserDTO);

                if (isSucess) {
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        out.print("{\"status\": \"error\", \"message\": \"Falha na atualização.\"}");
                }
        }

        @Override
        protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                
                String userIdString = request.getParameter("userid");

                if (userIdString == null || userIdString.trim().isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\": \"Id do usuario é obrigatório\"}");
                        return;
                }

                Long userIdParam;
                try {
                        userIdParam = Long.valueOf(userIdString);
                } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\": \"Id do usuario inválido\"}");
                        return;
                }

                Boolean isSuccess = userService.deleteUser(userIdParam);

                if (isSuccess) {
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"status\": \"error\", \"message\": \"Usuário com ID " + userIdParam + " não encontrado.\"}");
                }

                out.flush();
        }
}
