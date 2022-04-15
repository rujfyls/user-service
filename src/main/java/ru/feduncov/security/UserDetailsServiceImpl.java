package ru.feduncov.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.feduncov.entity.User;
import ru.feduncov.exceptions.UserNotFoundException;
import ru.feduncov.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
        User user = repository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с email=" + email + " не найден"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), getGrantedAuthority(user));
    }

    private Collection<GrantedAuthority> getGrantedAuthority(User user) {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        if (user.getRole().getName().equalsIgnoreCase("admin")) {
            collection.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            collection.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return collection;
    }
}
