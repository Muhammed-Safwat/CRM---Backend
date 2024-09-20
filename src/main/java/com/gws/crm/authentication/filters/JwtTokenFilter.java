package com.gws.crm.authentication.filters;

import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.authentication.utils.JwtTokenService;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.handler.ApiResponseHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            log.info("done first ");
            String token = getTokenFromRequest(request);

            if (token == null || (token != null && jwtTokenService.isTokenExpired(token))) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!jwtTokenService.isTokenExpired(token)) {
                Long userId = jwtTokenService.extractUserId(token);
                UserDetails userDetails = userRepository.findById(userId)
                        .orElseThrow(NotFoundResourceException::new);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                        null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

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
