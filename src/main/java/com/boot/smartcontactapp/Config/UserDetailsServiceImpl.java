package com.boot.smartcontactapp.Config;

import com.boot.smartcontactapp.Entities.User;
import com.boot.smartcontactapp.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService  {
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetching user from database
        User user =   userRepo.getUserByUserName(username);
        if(user==null){
            throw new UsernameNotFoundException("Could not found user!");
        }
        CustomUserDetailsImpl customUserDetails = new CustomUserDetailsImpl(user);
        return customUserDetails;
    }
}
