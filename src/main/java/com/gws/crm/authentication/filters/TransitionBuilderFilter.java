package com.gws.crm.authentication.filters;


import com.gws.crm.authentication.utils.JwtTokenService;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.utils.TransitionUtilHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class TransitionBuilderFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final TransitionUtilHandler transitionUtilHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            log.info("Transition filter started");
            String token = getTokenFromRequest(request);

            if (token == null || jwtTokenService.isTokenExpired(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            Long userId = jwtTokenService.extractUserId(token);
            String role = jwtTokenService.extractUserRole(token);
            String userName = jwtTokenService.extractUserName(token);

            int os = request.getHeader("os") != null ? Integer.parseInt(request.getHeader("os")) : 0;
            Transition transition = new Transition(
                    transitionUtilHandler.validateLanguage(request.getHeader("lang")),
                    userId, userName, role, os, request.getHeader("version")
            );

            request.setAttribute("transition", transition);
            filterChain.doFilter(request, response);

        } catch (NotFoundResourceException ex) {
            log.error("User not found: {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("application/json");
            response.getWriter().write("User not found");
        } catch (JwtValidationException ex) {
            log.error("JWT validation error: {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("Invalid or expired token");
        } catch (Exception ex) {
            log.error("Internal server error: {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.contains("/auth");
    }
}
