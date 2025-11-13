package dev.smd.estela.backend.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthFilter implements Filter {

        private static final List<String> PUBLIC_ROUTES = Arrays.asList(
                  "/api/login",
                  "/api/register"
        );
        
        @Override
        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
                  throws IOException, ServletException {

                HttpServletRequest request = (HttpServletRequest) req;
                HttpServletResponse response = (HttpServletResponse) res;

                String path = request.getRequestURI().substring(request.getContextPath().length());

                if (isPublicRoute(path)) {
                        chain.doFilter(request, response);
                } else {
                        HttpSession session = request.getSession(false);
                        if (session == null || session.getAttribute("user") == null) {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.setContentType("application/json");
                                response.setCharacterEncoding("UTF-8");
                                String jsonResponse = "{\"status\": 401, \"message\": \"Invalid or expired session.\"}";
                                response.getWriter().write(jsonResponse);
                        } else {
                                chain.doFilter(request, response);
                        }
                }
        }

        private boolean isPublicRoute(String path) {
                return PUBLIC_ROUTES.contains(path);
        }
}
