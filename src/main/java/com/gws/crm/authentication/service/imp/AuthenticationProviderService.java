package com.gws.crm.authentication.service.imp;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationProviderService implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Load user once and validate
        UserDetails user = userDetailsService.loadUserByUsername(username);
        validateUser(user);

        // Check the password
        if (passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        }

        throw new BadCredentialsException("Invalid username or password");
    }


    private void validateUser(UserDetails user) {
        if (!user.isEnabled()) {
            throw new DisabledException("User is not enabled");
        }

        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("User account is expired");
        }

        if (!user.isAccountNonLocked()) {
            throw new LockedException("User account is locked");
        }

        if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("User credentials are expired");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
