package ru.feduncov.service;

import org.springframework.stereotype.Service;
import ru.feduncov.entity.Role;
import ru.feduncov.exceptions.RoleNotFoundException;
import ru.feduncov.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException("Роли с именем = " + name + " не существует!"));
    }

    public Role getRole(long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Роли с id = " + id + " не существует!"));
    }

    public void addNewRole(Role role) {
        roleRepository.save(role);
    }

    public void updateRole(Role role) {
        roleRepository.save(role);
    }

    public void deleteAllRoles() {
        roleRepository.deleteAll();
    }

    public void deleteRole(long id) {
        roleRepository.deleteById(id);
    }
}
