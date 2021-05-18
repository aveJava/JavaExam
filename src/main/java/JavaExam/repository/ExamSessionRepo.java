package JavaExam.repository;

import JavaExam.domain.ExamSession;
import JavaExam.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamSessionRepo extends JpaRepository<ExamSession, Long> {
    List<ExamSession> findByUser(User user);
    List<ExamSession> findByDate(LocalDate date);
    List<ExamSession> findByDateBetween(LocalDate start, LocalDate end);
}
