/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package dev.smd.estela.backend.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import dev.smd.estela.backend.model.LoginCredentials;
import dev.smd.estela.backend.model.User;
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
                PrintWriter out = response.getWriter();

                // 1. Tenta fazer o parsing do JSON diretamente a partir do leitor
                LoginCredentials credentials;
                try {
                        // Gson lê o corpo da requisição (request.getReader()) e converte
                        // para o objeto LoginCredentials.
                        credentials = gson.fromJson(request.getReader(), LoginCredentials.class);

                        if (credentials == null) {
                                // Caso o corpo da requisição esteja vazio ou não seja um JSON válido.
                                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                out.print("{\"error\": \"Corpo da requisição ausente ou JSON inválido.\"}");
                                return;
                        }
                } catch (JsonIOException | JsonSyntaxException | IOException e) {
                        // Captura erros de parsing (ex: JSON mal formatado)
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

        // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
        /**
         * Handles the HTTP <code>GET</code> method.
         *
         * @param request servlet request
         * @param response servlet response
         * @throws ServletException if a servlet-specific error occurs
         * @throws IOException if an I/O error occurs
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                System.out.println("asd");
                processRequest(request, response);
        }

        /**
         * Returns a short description of the servlet.
         *
         * @return a String containing servlet description
         */
        @Override
        public String getServletInfo() {
                return "Short description";
        }// </editor-fold>

}
