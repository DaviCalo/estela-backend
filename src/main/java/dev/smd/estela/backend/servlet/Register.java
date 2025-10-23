package dev.smd.estela.backend.servlet;

import dev.smd.estela.backend.model.User;
import dev.smd.estela.backend.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(name = "Register", urlPatterns = {"/api/register"})
public class Register extends HttpServlet {

        private final AuthService authService = new AuthService();

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();

                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String name = request.getParameter("name");
                String nickName = request.getParameter("nickName");

                if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\": \"Nome de usuário e senha são obrigatórios para o cadastro.\"}");
                        return;
                }

                Optional<User> newUser = authService.register(username, password, name, nickName);

                if (newUser.isPresent()) {
                        User user = newUser.get();
                        response.setStatus(HttpServletResponse.SC_CREATED);

                        String jsonResponse = String.format(
                                  "{\"status\": \"success\", \"message\": \"Cadastro bem-sucedido!\", \"username\": \"%s\", \"role\": \"%b\"}",
                                  user.getUsername(), user.isAdministrator()
                        );
                        out.print(jsonResponse);

                } else {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        out.print("{\"status\": \"error\", \"message\": \"Falha no cadastro. O nome de usuário pode já estar em uso.\"}");
                }
        }
}
