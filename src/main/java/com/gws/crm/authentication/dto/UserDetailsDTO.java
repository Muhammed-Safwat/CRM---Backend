package com.gws.crm.authentication.dto;

import com.gws.crm.authentication.entity.Privilege;
import com.gws.crm.authentication.entity.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@ToString
public class UserDetailsDTO implements UserDetails {
    private Long id;
    private String name;
    private String username;
    private String password;
    private boolean locked;
    private boolean enabled;
    private boolean deleted;
    private LocalDateTime accountNonExpired;
    private LocalDateTime credentialsNonExpired;
    private Set<Role> roles;
    private Set<Privilege> privileges;

    public UserDetailsDTO(Long id,
                          String name,
                          String username,
                          String password,
                          boolean locked,
                          boolean enabled,
                          boolean deleted,
                          LocalDateTime accountNonExpired,
                          LocalDateTime credentialsNonExpired,
                          Set<Role> roles,
                          Set<Privilege> privileges) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.locked = locked;
        this.enabled = enabled;
        this.deleted = deleted;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.roles = roles;
        this.privileges = privileges;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }
        for (Privilege privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority("PRIV_" + privilege.getName()));
        }
        return authorities;
    }

    public Collection<? extends GrantedAuthority> getMainRoles() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired.isAfter(LocalDateTime.now());
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired.isAfter(LocalDateTime.now());
    }

    @Override
    public boolean isEnabled() {
        return enabled && !deleted;
    }
}
