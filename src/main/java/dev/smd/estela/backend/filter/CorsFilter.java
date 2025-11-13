package dev.smd.estela.backend.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }

        @Override
        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
                HttpServletResponse response = (HttpServletResponse) res;
                HttpServletRequest request = (HttpServletRequest) req;
                String origin = request.getHeader("Origin");
                if (origin != null) {
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
