package com.gws.crm.authentication.filters;


import com.gws.crm.authentication.utils.JwtTokenService;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.handler.ApiResponseHandler;
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        try {
            log.info("done s ");
            String token = getTokenFromRequest(request);

            if (token == null) filterChain.doFilter(request, response);

            Long userId = jwtTokenService.extractUserId(token);

            int os = (request.getHeader("os") != null && !request.getHeader("os").isEmpty())
                    ? Integer.parseInt(request.getHeader("os")) : 0;
            Transition transition = null;
            try {
                transition = new Transition(transitionUtilHandler.validateLanguage(request.getHeader("lang")),
                        userId, os, request.getHeader("version"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            request.setAttribute("transition", transition);
            filterChain.doFilter(request, response);
        } catch (NotFoundResourceException ex) {
            // Handle NotFoundResourceException
            log.error("User not found: {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            ApiResponseHandler.notFound("User not found").getBody();
        } catch (JwtValidationException ex) {
            // Handle JwtValidationException
            log.error("JWT validation error: {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ApiResponseHandler.unauthorized("Invalid or expired token").getBody();
        } catch (Exception ex) {
            // Handle general exceptions
            log.error("Internal server error: {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ApiResponseHandler.internalServerError().getBody();
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
