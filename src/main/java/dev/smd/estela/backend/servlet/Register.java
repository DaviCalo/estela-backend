package dev.smd.estela.backend.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import dev.smd.estela.backend.dto.NewUserDTO;
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
        private final Gson gson = new Gson();

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                
                NewUserDTO newUserDTO;
                try {
                        newUserDTO = gson.fromJson(request.getReader(), NewUserDTO.class);
                        if (newUserDTO == null) {
                                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                out.print("{\"error\": \"Corpo da requisição ausente ou JSON inválido.\"}");
                                return;
                        }
                } catch (JsonIOException | JsonSyntaxException | IOException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\": \"JSON mal formatado: " + e.getMessage() + "\"}");
                        return;
                }

                String username = newUserDTO.getEmail();
                String password = newUserDTO.getPassword();
                String name = newUserDTO.getName();
                String nickName = newUserDTO.getNickName();

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
                                  user.getEmail(), user.getAdministrator()
                        );
                        out.print(jsonResponse);

                } else {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        out.print("{\"status\": \"error\", \"message\": \"Falha no cadastro. O nome de usuário pode já estar em uso.\"}");
                }
        }
}
