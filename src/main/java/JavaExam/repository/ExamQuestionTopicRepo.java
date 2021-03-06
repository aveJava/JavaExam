package JavaExam.repository;

import JavaExam.domain.ExamQuestionFieldOfKnowledge;
import JavaExam.domain.ExamQuestionTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamQuestionTopicRepo extends JpaRepository<ExamQuestionTopic, Long> {
    List<ExamQuestionTopic> findByFieldOfKnowledge(ExamQuestionFieldOfKnowledge fieldOfKnowledge);
    List<ExamQuestionTopic> findByFieldOfKnowledgeName(String fieldOfKnowledgeName);
    List<ExamQuestionTopic> findByNameContainingIgnoreCase(String nameInclusion);
    ExamQuestionTopic findByFieldOfKnowledgeNameAndName(String fieldOfKnowledgeName, String topicName);
}
