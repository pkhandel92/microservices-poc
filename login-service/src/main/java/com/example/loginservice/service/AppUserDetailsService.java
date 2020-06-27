package com.example.loginservice.service;

import com.example.loginservice.dto.UserCreds;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private UserCreds userCredential;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserBuilder builder = null;
        if(null != userCredential){
            builder = User.withUsername(userCredential.getUsername());
            builder.password(userCredential.getPassword().toString());
            builder.roles("ADMIN");
        }
        else{
            throw new UsernameNotFoundException("User not found");
        }
        return builder.build();
    }

    public void setUserCredentials(UserCreds userCredential){
        this.userCredential = userCredential;
    }
}
