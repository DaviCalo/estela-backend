package dev.smd.estela.backend.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import dev.smd.estela.backend.dto.LoginCredentialsDTO;
import dev.smd.estela.backend.model.User;
import dev.smd.estela.backend.service.AuthService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@WebServlet(name = "LoginServlet", urlPatterns = {"/api/login"})
public class LoginServlet extends HttpServlet {

        private final AuthService authService = new AuthService();
        private final Gson gson = new Gson();

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                PrintWriter out = response.getWriter();

                LoginCredentialsDTO credentials;
                try {
                        credentials = gson.fromJson(request.getReader(), LoginCredentialsDTO.class);
                        if (credentials == null) {
                                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                out.print("{\"error\": \"Corpo da requisição ausente ou JSON inválido.\"}");
                                return;
                        }
                } catch (JsonIOException | JsonSyntaxException | IOException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\": \"JSON mal formatado: " + e.getMessage() + "\"}");
                        return;
                }

                String email = credentials.getEmail();
                String password = credentials.getPassword();

                if (email == null || password == null) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\": \"Nome de usuário e senha são obrigatórios.\"}");
                        return;
                }

                Optional<User> authenticatedUserOptional = authService.authenticate(email, password);

                if (authenticatedUserOptional.isPresent()) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        HttpSession sessao = request.getSession(true);
                        sessao.setMaxInactiveInterval(30 * 600);
                        User authenticatedUser = authenticatedUserOptional.get();
                        sessao.setAttribute("user", authenticatedUser);
                        String jsonResponse = String.format(
                                  "{\"status\": \"success\", \"user\": {\"username\": \"%s\", \"isADM\": \"%b\", \"userid\": %d}}",
                                  authenticatedUser.getEmail(),
                                  authenticatedUser.getAdministrator(),
                                  authenticatedUser.getUserId()
                        );
                        System.out.println(jsonResponse);
                        out.print(jsonResponse);
                } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        out.print("{\"status\": \"error\", \"message\": \"Credenciais inválidas.\"}");
                }
        }
}
