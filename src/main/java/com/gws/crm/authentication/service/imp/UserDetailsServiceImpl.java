package com.gws.crm.authentication.service.imp;

import com.gws.crm.authentication.dto.UserDetailsDTO;
import com.gws.crm.authentication.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDetailsDTO> optionalUserDetailsDTO = userRepository.findDTOByUsername(username);
        if (optionalUserDetailsDTO.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username or password !");
        }
        return optionalUserDetailsDTO.get();
    }

}
