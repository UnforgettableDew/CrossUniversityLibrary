package com.crossuniversity.securityservice.security;

import com.crossuniversity.securityservice.entity.UserCredentials;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class AppUserDetails implements UserDetails {
    private final Set<? extends GrantedAuthority> grantedAuthorities;
    private final String password;
    private final String email;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;
    private final boolean isEnabled;

    public AppUserDetails(Set<? extends GrantedAuthority> grantedAuthorities,
                          String password,
                          String email,
                          boolean isAccountNonExpired,
                          boolean isAccountNonLocked,
                          boolean isCredentialsNonExpired,
                          boolean isEnabled) {
        this.grantedAuthorities = grantedAuthorities;
        this.password = password;
        this.email = email;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public static AppUserDetails convertToUserDetails(UserCredentials userCredentials) {
        return new AppUserDetails(
                Set.of(new SimpleGrantedAuthority("ROLE_" + userCredentials.getRole().getRoleName())),
                userCredentials.getPassword(),
                userCredentials.getEmail(),
                true,
                true,
                true,
                true
        );
    }
}
