package ru.feduncov.repository;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.feduncov.entity.Role;
import ru.feduncov.entity.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findUserByEmail() {
        assertTrue(userRepository.findAll().iterator().hasNext());

        Role role = roleRepository.save(new Role("USER"));
        User user = userRepository.save(new User("name", "email", "password", role));

        entityManager.persist(role);
        entityManager.persist(user);
        entityManager.flush();

        assertEquals(userRepository.findUserByEmail("email").orElse(new User()).getUsername(), user.getUsername());
    }
}