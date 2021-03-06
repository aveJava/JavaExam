package JavaExam.service;

import JavaExam.domain.ExamQuestionFieldOfKnowledge;
import JavaExam.domain.ExamQuestionTopic;
import JavaExam.repository.ExamQuestionTopicRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamQuestionTopicService {

    private final ExamQuestionTopicRepo topicRepo;

    public ExamQuestionTopicService(ExamQuestionTopicRepo topicRepo) {
        this.topicRepo = topicRepo;
    }

    public ExamQuestionTopic get(Long id) {
        return topicRepo.getOne(id);
    }

    public List<ExamQuestionTopic> getAll() {
        return topicRepo.findAll();
    }

    public List<ExamQuestionTopic> findAllByFieldOfKnowledge(ExamQuestionFieldOfKnowledge fieldOfKnowledge) {
        return topicRepo.findByFieldOfKnowledge(fieldOfKnowledge);
    }
    public List<ExamQuestionTopic> findAllByFoKnName(String foKnName) {
        return topicRepo.findByFieldOfKnowledgeName(foKnName);
    }

    public ExamQuestionTopic findByFoKnNameAndName(String foKnName, String topicName) {
        return topicRepo.findByFieldOfKnowledgeNameAndName(foKnName, topicName);
    }

    public List<ExamQuestionTopic> findByNameContaining(String nameInclusion) {
        return topicRepo.findByNameContainingIgnoreCase(nameInclusion);
    }

    public boolean hasFindByFieldOfKnowledgeAndName(ExamQuestionFieldOfKnowledge fieldOfKnowledge, String name) {
        return findByFoKnNameAndName(fieldOfKnowledge.getName(), name) != null;
    }

    public boolean save(ExamQuestionTopic topic) {
        topicRepo.save(topic);
        return true;
    }

    public boolean update(ExamQuestionTopic topic) {
        Long id = topic.getId();
        if (topicRepo.findById(id) == null) {
            return false;   // нечего обновлять
        }
        topicRepo.save(topic);
        return true;
    }

    public boolean delete(ExamQuestionTopic topic) {
        Long id = topic.getId();
        if (topicRepo.findById(id) == null) {
            return false;   // нечего удалять
        }
        topicRepo.delete(topic);
        return true;
    }

}
