package JavaExam.repository;

import JavaExam.domain.ExamSession;
import JavaExam.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamSessionRepo extends JpaRepository<ExamSession, Long> {
    @Transactional
    List<ExamSession> findByUser(User user);
    @Transactional
    List<ExamSession> findByDate(LocalDate date);
    @Transactional
    List<ExamSession> findByDateBetween(LocalDate start, LocalDate end);
    @Transactional
    List<ExamSession> findByIsCompletedIsTrue();
}
