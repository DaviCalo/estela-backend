package dev.smd.estela.backend.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class CorsFilter implements Filter {
    private static final String ALLOWED_ORIGIN_1 = "http://localhost:3000";
    private static final String ALLOWED_ORIGIN_2 = "http://192.168.100.4:3000"; 

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        
        String origin = request.getHeader("Origin");
        
        if (origin != null && (origin.equals(ALLOWED_ORIGIN_1) || origin.equals(ALLOWED_ORIGIN_2))) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }
        
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        
        response.setHeader("Access-Control-Allow-Headers", "authorization, content-type, accept");
        
        response.setHeader("Access-Control-Allow-Credentials", "true");
        
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        chain.doFilter(req, res);
    }
}