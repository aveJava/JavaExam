package JavaExam.service;

import JavaExam.domain.ExamQuestion;
import JavaExam.domain.ExamQuestionFieldOfKnowledge;
import JavaExam.domain.ExamQuestionTopic;
import JavaExam.repository.ExamQuestionRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamQuestionService {

    ExamQuestionRepo questionRepo;

    public ExamQuestionService(ExamQuestionRepo questionRepo) {
        this.questionRepo = questionRepo;
    }

    public ExamQuestion get(Long id) {
        return questionRepo.getOne(id);
    }

    public List<ExamQuestion> getAll() {
        return questionRepo.findAll();
    }

    public List<ExamQuestion> getAllByTopic(ExamQuestionTopic topic) {
        return questionRepo.findByTopic(topic);
    }

    public List<ExamQuestion> getAllByFieldOfKnowledge(ExamQuestionFieldOfKnowledge fieldOfKnowledge) {
        return questionRepo.findByTopicFieldOfKnowledge(fieldOfKnowledge);
    }

    public boolean save(ExamQuestion question) {
        questionRepo.save(question);
        return true;
    }

    public boolean update(ExamQuestion question) {
        Long id = question.getId();
        if (questionRepo.findById(id) == null) {
            return false;   // нечего обновлять
        }
        questionRepo.save(question);
        return true;
    }

    public boolean delete(ExamQuestion question) {
        Long id = question.getId();
        if (questionRepo.findById(id) == null) {
            return false;   // нечего удалять
        }
        questionRepo.delete(question);
        return true;
    }

}
