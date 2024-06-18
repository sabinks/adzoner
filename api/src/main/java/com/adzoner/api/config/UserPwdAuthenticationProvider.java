package com.adzoner.api.config;

import com.adzoner.api.entity.Role;
import com.adzoner.api.entity.User;
import com.adzoner.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserPwdAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        List<User> userList = userRepository.findByEmail(username);

        if (!userList.isEmpty()) {
            if (passwordEncoder.matches(password, userList.get(0).getPassword())) {
                List<GrantedAuthority> authorities = (List<GrantedAuthority>) getAuthorities(userList.get(0).getRoles());

                return new UsernamePasswordAuthenticationToken(username, password, authorities);
            } else {

                throw new BadCredentialsException("Invalid Password!");
            }
        } else {

            throw new BadCredentialsException("No user registered with the details!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {

        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private List<? extends GrantedAuthority> getAuthorities(List<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorities;
    }
}
