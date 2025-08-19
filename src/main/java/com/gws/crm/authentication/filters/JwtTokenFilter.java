package com.gws.crm.authentication.filters;

import com.gws.crm.authentication.dto.UserDetailsDTO;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.authentication.utils.JwtTokenService;
import com.gws.crm.common.exception.NotFoundResourceException;
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
            log.info("=== Transition filter started for: {} ===", request.getRequestURI());
            String token = getTokenFromRequest(request);

            if (token == null) {
                log.info("No token found, proceeding without auth");
                filterChain.doFilter(request, response);
                return;
            }

            boolean isExpired = jwtTokenService.isTokenExpired(token);
            log.info("Token expired check: {}", isExpired);

            if (isExpired) {
                log.warn("Token is expired - returning 401");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token expired\"}");
                return;
            }

            long userId = jwtTokenService.extractUserId(token);
//            UserDetails userDetails = userRepository.findByIdWithRoles(userId)
//                    .orElseThrow(NotFoundResourceException::new);
            UserDetailsDTO userDetails = userRepository.findUserDetailById(userId)
                    .orElseThrow(NotFoundResourceException::new);
            if (!isUserValid(userDetails)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("Account expired or missing");
                return;
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(), null, userDetails.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

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

    private boolean isUserValid(UserDetails user) {
        return user.isEnabled() && user.isAccountNonExpired() &&
                user.isAccountNonLocked() && user.isCredentialsNonExpired();
    }

    private boolean isUserValid(UserDetailsDTO user) {
        return user.isEnabled() && user.isAccountNonExpired() &&
                user.isAccountNonLocked() && user.isCredentialsNonExpired();
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
        String method = request.getMethod();
        return path.contains("/auth") || (path.contains("/images/") && "GET".equals(method));
    }
}

