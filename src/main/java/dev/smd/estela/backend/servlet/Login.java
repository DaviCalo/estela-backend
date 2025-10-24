package dev.smd.estela.backend.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import dev.smd.estela.backend.dto.LoginCredentialsDTO;
import dev.smd.estela.backend.entity.User;
import dev.smd.estela.backend.service.AuthService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

@WebServlet(name = "login", urlPatterns = {"/api/login"})
public class Login extends HttpServlet {

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

                String username = credentials.getUsername();
                String password = credentials.getPassword();

                System.out.println(username + " " + password);

                if (username == null || password == null) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\": \"Nome de usuário e senha são obrigatórios.\"}");
                        return;
                }

                Optional<User> authenticatedUser = authService.authenticate(username, password);

                if (authenticatedUser.isPresent()) {
                        User user = authenticatedUser.get();
                        response.setStatus(HttpServletResponse.SC_OK);
                        String jsonResponse = String.format(
                                  "{\"status\": \"success\", \"message\": \"Login bem-sucedido!\", \"username\": \"%s\", \"role\": \"%b\"}",
                                  user.getUsername(), user.isAdministrator()
                        );
                        out.print(jsonResponse);

                } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        out.print("{\"status\": \"error\", \"message\": \"Credenciais inválidas.\"}");
                }
        }

        protected void processRequest(HttpServletRequest request, HttpServletResponse response)
                  throws ServletException, IOException {
                response.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                        /* TODO output your page here. You may use following sample code. */
                        out.println("<!DOCTYPE html>");
                        out.println("<html>");
                        out.println("<head>");
                        out.println("<title>Servlet Login</title>");
                        out.println("</head>");
                        out.println("<body>");
                        out.println("<h1>Servlet Login at " + request.getContextPath() + "</h1>");
                        out.println("</body>");
                        out.println("</html>");
                }
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                processRequest(request, response);
        }

        @Override
        public String getServletInfo() {
                return "Short description";
        }
}
