package JavaExam.service;

import JavaExam.domain.User;
import JavaExam.repository.RoleRepo;
import JavaExam.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepo userRepo, RoleRepo roleRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User get(long id) {
        return userRepo.getOne(id);
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public List<User> findAllByUsername(String username) {
        return userRepo.findAllByUsername(username);
    }

    public boolean isPresentEnableUsersWithUsername(String username) {
        return userRepo.findByUsernameAndEnabledIsTrue(username).isPresent();
    }

    public boolean save(User user) {
        // если в БД уже есть активный пользователь с таким именем, то ничего не делать
        if (isPresentEnableUsersWithUsername(user.getUsername())) {
            return false;
        }

        // хеширование пароля, установка новому пользователю роли User и сохраниние в БД
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(roleRepo.findByName("ROLE_USER")));
        user.setEnabled(true);
        userRepo.save(user);

        return true;
    }

    public boolean update(User user) {
        // если такого пользователя нет в БД - ничего не делать
        Optional<User> optional = userRepo.findById(user.getId());
        if (!optional.isPresent()) return false;

        User userFromDB = optional.get();
        String hashPass = bCryptPasswordEncoder.encode(user.getPassword());

        // если пароль из формы не совпадает с паролем из БД - ничего не делать
        if (!hashPass.equals(userFromDB.getPassword())) {
            return false;
        }

        // обновить имя или пароль, если в форме на обновление были указаны новые имя или пароль
        if (user.getNewUserName() != null) user.setUsername(user.getNewUserName());
        if (user.getNewPassword() != null) user.setPassword(bCryptPasswordEncoder.encode(user.getNewUserName()));

        userRepo.save(user);

        return true;
    }

    public void delete(User user) {
        userRepo.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> usr = userRepo.findByUsernameAndEnabledIsTrue(username);
        if (!usr.isPresent()) throw new UsernameNotFoundException("User not found");

        return usr.get();    // UserEntity реализует interface UserDetails
    }

}
