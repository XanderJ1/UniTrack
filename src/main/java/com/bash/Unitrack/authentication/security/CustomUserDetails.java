package com.bash.Unitrack.authentication.security;

import com.bash.Unitrack.authentication.model.Role;
import com.bash.Unitrack.authentication.model.User;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isEnabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public static CustomUserDetails build(User user){
        return new CustomUserDetails(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                mapRoles(user.getRole())
        );
    }

    private static Collection<? extends GrantedAuthority> mapRoles(Role role){
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
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
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
