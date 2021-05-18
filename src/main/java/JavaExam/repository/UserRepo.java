package JavaExam.repository;

import JavaExam.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndEnabledIsTrue(String username);
    List<User> findAllByUsername(String username);
}
