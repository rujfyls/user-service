package ru.feduncov.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.feduncov.controller.dto.EditRoleRequestDTO;
import ru.feduncov.controller.dto.UserRequestDTO;
import ru.feduncov.controller.dto.UserResponseDTO;
import ru.feduncov.entity.User;
import ru.feduncov.exceptions.ValidUserException;
import ru.feduncov.service.RoleService;
import ru.feduncov.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers().stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable long id) {
        return ResponseEntity.ok(new UserResponseDTO(userService.getUserById(id)));
    }

    @PostMapping
    public ResponseEntity<Long> addNewUser(@RequestBody @Valid UserRequestDTO requestDTO, BindingResult result) {
        if (result.hasErrors()) {
            for (Object o : result.getAllErrors()) {
                FieldError fieldError = (FieldError) o;
                throw new ValidUserException(fieldError.getDefaultMessage());
            }
        }
        User user = new User();
        user.setEmail(requestDTO.getEmail());
        user.setUsername(requestDTO.getName());
        user.setPassword(encoder.encode(requestDTO.getPassword()));
        user.setRole(roleService.getRoleByName("USER"));

        return ResponseEntity.ok(userService.createOrUpdateUser(user));
    }

    @PostMapping("/edit")
    public ResponseEntity<Long> editRole(@RequestBody EditRoleRequestDTO requestDTO) {
        User user = userService.getUserById(requestDTO.getUserId());
        user.setRole(roleService.getRoleByName(requestDTO.getRole().toUpperCase()));

        return ResponseEntity.ok(userService.createOrUpdateUser(user));
    }

    @PostMapping("/{id}")
    public ResponseEntity addNewUserForId(@PathVariable long id) {
        return ResponseEntity.badRequest().body(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @PutMapping
    public ResponseEntity<Long> updateUser(@RequestBody @Valid UserRequestDTO requestDTO, BindingResult result) {
        if (result.hasErrors()) {
            for (Object o : result.getAllErrors()) {
                FieldError fieldError = (FieldError) o;
                throw new ValidUserException(fieldError.getDefaultMessage());
            }
        }

        User user = userService.getUserByEmail(requestDTO.getEmail());

        if (user == null) {
            user = new User();
            user.setEmail(requestDTO.getEmail());
            user.setRole(roleService.getRoleByName("USER"));
        }

        user.setUsername(requestDTO.getName());
        user.setPassword(encoder.encode(requestDTO.getPassword()));

        return ResponseEntity.ok(userService.createOrUpdateUser(user));
    }


    @DeleteMapping
    public void deleteAllUsers() {
        userService.deleteAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUserById(id);
    }
}
