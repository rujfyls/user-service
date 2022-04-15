package ru.feduncov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.feduncov.controller.dto.EditRoleRequestDTO;
import ru.feduncov.controller.dto.UserRequestDTO;
import ru.feduncov.controller.dto.UserResponseDTO;
import ru.feduncov.entity.Role;
import ru.feduncov.entity.User;
import ru.feduncov.security.UserDetailsServiceImpl;
import ru.feduncov.service.RoleService;
import ru.feduncov.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @WithMockUser(username = "dexter_aljp1@gmail.com", roles = "ADMIN")
    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(getTestUsers());

        String expected = objectMapper.writeValueAsString(getTestUsers()
                .stream().map(UserResponseDTO::new).collect(Collectors.toList()));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @WithMockUser(username = "dexteraljp1@gmail.com", roles = "USER")
    @Test
    void getUser() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(getTestUsers().get(0));

        String expected = objectMapper.writeValueAsString(new UserResponseDTO(getTestUsers().get(0)));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected))
                .andExpect(jsonPath("$.id", equalTo(1)));
    }

    @WithMockUser(username = "dexteraljp1@gmail.com", roles = "USER")
    @Test
    void addNewUser() throws Exception {
        User u = new User("alex", "alex@mail.ru", "password", new Role("ADMIN"));
        when(userService.createOrUpdateUser(u)).thenReturn(anyLong());

        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setName(u.getUsername());
        requestDTO.setEmail(u.getEmail());
        requestDTO.setPassword(u.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithMockUser(username = "dexteraljp1@gmail.com", roles = "USER")
    @Test
    void addNewUserWithException() throws Exception {
        User u = new User(null, null, null, new Role("ADMIN"));
        when(userService.createOrUpdateUser(u)).thenReturn(null);

        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setName(u.getUsername());
        requestDTO.setEmail(u.getEmail());
        requestDTO.setPassword(u.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @WithMockUser(username = "dexter_aljp1@gmail.com", roles = "ADMIN")
    @Test
    void editRole() throws Exception {
        User u = getTestUsers().get(0);
        when(userService.getUserById(u.getUserId())).thenReturn(u);
        when(roleService.getRoleByName("USER")).thenReturn(new Role("USER"));

        EditRoleRequestDTO requestDTO = new EditRoleRequestDTO();
        requestDTO.setRole("USER");
        requestDTO.setUserId(u.getUserId());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithMockUser(username = "dexteraljp1@gmail.com", roles = "USER")
    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/users/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private List<User> getTestUsers() {
        return Arrays.asList(
                new User(1L, "alex", "alex.mail.ru", "password", new Role("ADMIN")),
                new User(2L, "kate", "kate.mail.ru", "password", new Role("USER")),
                new User(3L, "vlad", "vlad.mail.ru", "password", new Role("USER"))
        );
    }
}
