package JavaExam.service;

import JavaExam.domain.ExamSession;
import JavaExam.domain.User;
import JavaExam.repository.ExamSessionRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExamSessionService {

    private final ExamSessionRepo sessionRepo;

    public ExamSessionService(ExamSessionRepo sessionRepo) {
        this.sessionRepo = sessionRepo;
    }

    public ExamSession get(Long id) {
        return sessionRepo.getOne(id);
    }

    public List<ExamSession> getAll() {
        return sessionRepo.findAll();
    }

    public List<ExamSession> findByDate(LocalDate date) {
        return sessionRepo.findByDate(date);
    }

    public List<ExamSession> findByDateBetween(LocalDate start, LocalDate end) {
        return sessionRepo.findByDateBetween(start, end);
    }

    public List<ExamSession> findByUser(User user) {
        return sessionRepo.findByUser(user);
    }

    public ExamSession save(ExamSession topic) {
        return sessionRepo.save(topic);
    }

    public boolean update(ExamSession topic) {
        Long id = topic.getId();
        if (sessionRepo.findById(id) == null) {
            return false;   // нечего обновлять
        }
        sessionRepo.save(topic);
        return true;
    }

    public boolean delete(ExamSession topic) {
        Long id = topic.getId();
        if (sessionRepo.findById(id) == null) {
            return false;   // нечего удалять
        }
        sessionRepo.delete(topic);
        return true;
    }

}
