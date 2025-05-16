package com.comission.comission.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepo;

    @Autowired
    public AppUserService(AppUserRepository appUserRepo)
    {
        this.appUserRepo=appUserRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = appUserRepo.findByUsername(username);

        if(appUser.isEmpty())
        {
            throw new UsernameNotFoundException("username is not found");
        }
        return appUser.get();
    }
}
