package ru.feduncov.service;

import org.springframework.stereotype.Service;
import ru.feduncov.entity.User;
import ru.feduncov.exceptions.UserNotFoundException;
import ru.feduncov.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + id + " не существует!"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователя с email = " + email + " не существует!"));
    }

    public Long createOrUpdateUser(User user) {
        return userRepository.save(user).getUserId();
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    public void deleteUserById(long id) {
        User user = getUserById(id);
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
