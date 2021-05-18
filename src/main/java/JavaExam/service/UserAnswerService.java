package JavaExam.service;

import JavaExam.domain.UserAnswer;
import JavaExam.repository.UserAnswerRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAnswerService {

    UserAnswerRepo answerRepo;

    public UserAnswerService(UserAnswerRepo answerRepo) {
        this.answerRepo = answerRepo;
    }

    public UserAnswer get(Long id) {
        return answerRepo.getOne(id);
    }

    public List<UserAnswer> getAll() {
        return answerRepo.findAll();
    }

    public boolean save(UserAnswer topic) {
        answerRepo.save(topic);
        return true;
    }

    public boolean update(UserAnswer topic) {
        Long id = topic.getId();
        if (answerRepo.findById(id) == null) {
            return false;   // нечего обновлять
        }
        answerRepo.save(topic);
        return true;
    }

    public boolean delete(UserAnswer topic) {
        Long id = topic.getId();
        if (answerRepo.findById(id) == null) {
            return false;   // нечего удалять
        }
        answerRepo.delete(topic);
        return true;
    }

}
