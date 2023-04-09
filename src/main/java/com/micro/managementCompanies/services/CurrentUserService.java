package com.micro.managementCompanies.services;
/*
import com.micro.managementCompanies.models.CurrentUser;
import com.micro.managementCompanies.models.UserSystem;
import com.micro.managementCompanies.repositories.UserSystemRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService implements UserDetailsService {
    private final UserSystemRepository repository;

    public CurrentUserService(UserSystemRepository repository) {
        this.repository = repository;
    }

    @Override
    public CurrentUser loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserSystem user = repository.findByName(username);
        if (user != null) {
            final CurrentUser currentUser = new CurrentUser();
            currentUser.setName(user.getName());
            currentUser.setPassword(user.getPassword());

            return currentUser;
        }
        return null;
    }
}
*/