package JavaExam.service;

import JavaExam.domain.ExamQuestionFieldOfKnowledge;
import JavaExam.repository.ExamQuestionFieldOfKnowledgeRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamQuestionFieldOfKnowledgeService {

    ExamQuestionFieldOfKnowledgeRepo fieldKnowledgeRepo;

    public ExamQuestionFieldOfKnowledgeService(ExamQuestionFieldOfKnowledgeRepo fieldKnowledgeRepo) {
        this.fieldKnowledgeRepo = fieldKnowledgeRepo;
    }

    public ExamQuestionFieldOfKnowledge get(Long id) {
        return fieldKnowledgeRepo.getOne(id);
    }

    public List<ExamQuestionFieldOfKnowledge> getAll() {
        return fieldKnowledgeRepo.findAll();
    }

    public ExamQuestionFieldOfKnowledge getByName(String name) {
        return fieldKnowledgeRepo.findByName(name);
    }

    public boolean save(ExamQuestionFieldOfKnowledge fieldKnowledge) {
        String name = fieldKnowledge.getName();
        if (fieldKnowledgeRepo.findByName(name) != null) {
            return false;   // такая уже есть
        }
        fieldKnowledgeRepo.save(fieldKnowledge);
        return true;
    }

    public boolean update(ExamQuestionFieldOfKnowledge fieldKnowledge) {
        Long id = fieldKnowledge.getId();
        if (fieldKnowledgeRepo.findById(id) == null) {
            return false;   // нечего обновлять
        }
        fieldKnowledgeRepo.save(fieldKnowledge);
        return true;
    }

    public boolean delete(ExamQuestionFieldOfKnowledge fieldKnowledge) {
        Long id = fieldKnowledge.getId();
        if (fieldKnowledgeRepo.findById(id) == null) {
            return false;   // нечего удалять
        }
        fieldKnowledgeRepo.delete(fieldKnowledge);
        return true;
    }

}
