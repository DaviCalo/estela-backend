/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author maria
 */
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

        Optional<User> newUser = authService.register(username, password,  name,  nickName); 
        
        if (newUser.isPresent()) {
            // Registration successful
            User user = newUser.get();
            response.setStatus(HttpServletResponse.SC_CREATED); // 201 Created is often used for successful registration
            
            // Note: The `role` in your Login servlet was formatted with `%b`, suggesting it's a boolean.
            // I'm keeping the original pattern for consistency, but a role is often a String.
            String jsonResponse = String.format(
                "{\"status\": \"success\", \"message\": \"Cadastro bem-sucedido!\", \"username\": \"%s\", \"role\": \"%b\"}",
                user.getUsername(), user.isAdministrator()
            );
            out.print(jsonResponse);
            
        } else {
            // Registration failed (e.g., username already taken)
            response.setStatus(HttpServletResponse.SC_CONFLICT); // 409 Conflict is suitable for username already exists
            out.print("{\"status\": \"error\", \"message\": \"Falha no cadastro. O nome de usuário pode já estar em uso.\"}");
        }
    }
    
    // To keep the example concise, the doGet and boilerplate methods are omitted,
    // but you can include them from your original Login servlet if needed.
    
}
