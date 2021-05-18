package JavaExam.service;

import JavaExam.domain.Role;
import JavaExam.repository.RoleRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    RoleRepo roleRepo;

    public RoleService(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public List<Role> getAll() {
        return roleRepo.findAll();
    }

    public Role get(long id) {
        return roleRepo.getOne(id);
    }

    public Role save(Role role) {
        return roleRepo.save(role);
    }

    public void delete(Role role) {
        roleRepo.delete(role);
    }

}
