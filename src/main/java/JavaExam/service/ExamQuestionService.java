package JavaExam.service;

import JavaExam.domain.ExamQuestion;
import JavaExam.domain.ExamQuestionFieldOfKnowledge;
import JavaExam.domain.ExamQuestionTopic;
import JavaExam.repository.ExamQuestionRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamQuestionService {
    private final ExamQuestionFieldOfKnowledgeService foKnService;
    private final ExamQuestionTopicService topicService;

    private final ExamQuestionRepo questionRepo;

    public ExamQuestionService(ExamQuestionFieldOfKnowledgeService fieldOfKnowledgeService, ExamQuestionTopicService topicService, ExamQuestionRepo questionRepo) {
        this.foKnService = fieldOfKnowledgeService;
        this.topicService = topicService;
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

    public Page<ExamQuestion> search(String foKnName, String topicName, int pageNumber, int pageSize) {
        boolean isFoKnPresent = foKnName != null && !foKnName.isEmpty() && foKnService.getByName(foKnName) != null;
        boolean isTopicPresent = isFoKnPresent && topicName != null && !topicName.isEmpty() && topicService.findByFoKnNameAndName(foKnName, topicName) != null;

        Sort sort = Sort.by(Sort.Direction.ASC, isTopicPresent ? "id" : "topic");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, sort);
        if (isFoKnPresent && !isTopicPresent) return questionRepo.findByTopicFieldOfKnowledgeName(foKnName, pageRequest);
        if (isTopicPresent) return questionRepo.findByTopicFieldOfKnowledgeNameAndTopicName(foKnName, topicName, pageRequest);

        return questionRepo.findAll(pageRequest);
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
