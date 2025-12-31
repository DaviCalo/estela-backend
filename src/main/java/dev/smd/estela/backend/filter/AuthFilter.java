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
            "/api/register",
            "/api/game/*",
            "/api/games/*",
            "/api/checkout",
            "/api/my-games",
            "/api/logout",
            "/api/user"
    );

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI().substring(request.getContextPath().length());

        if (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        if (isPublicRoute(path)) {
            chain.doFilter(request, response);
        } else {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                String jsonResponse = "{\"status\": 401, \"message\": \"Sessão inválida ou expirada. Acesso requer autenticação.\"}";
                response.getWriter().write(jsonResponse);
            } else {
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {
    }

    private boolean isPublicRoute(String path) {
        for (String publicRoute : PUBLIC_ROUTES) {
            if (publicRoute.endsWith("/*")) {
                String prefix = publicRoute.substring(0, publicRoute.length() - 2);
                if (path.startsWith(prefix)) {
                    return true;
                }
            } else if (publicRoute.equals(path)) {
                return true;
            }
        }
        return false;
    }
}
