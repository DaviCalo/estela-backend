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

@WebServlet(name = "user", urlPatterns = {"/api/user"})
public class UserServlet extends HttpServlet {

        private final Gson gson = new Gson();
        private final UserService userService = new UserService();

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();

                Long userIdParam = Long.valueOf(request.getParameter("userid"));

                if (userIdParam == null) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\": \"Id do usuario é obrigatório\"}");
                        return;
                }

                User user = userService.getUserById(userIdParam);

                if (user == null) {
                        out.print("{\"status\": \"error\", \"message\": \"Usuário com ID " + userIdParam + " não encontrado.\"}");
                } else {
                        response.setStatus(HttpServletResponse.SC_OK);
                        String userJson = gson.toJson(user);
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

                System.out.println(updateUserDTO);

                Boolean isSucess = userService.updateUser(updateUserDTO);

                if (isSucess) {
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        out.print("{\"status\": \"error\", \"message\": \"Falha na atualização.\"}");
                }
        }
}
