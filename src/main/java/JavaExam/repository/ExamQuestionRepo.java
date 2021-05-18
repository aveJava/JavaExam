package JavaExam.repository;

import JavaExam.domain.ExamQuestion;
import JavaExam.domain.ExamQuestionFieldOfKnowledge;
import JavaExam.domain.ExamQuestionTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamQuestionRepo extends JpaRepository<ExamQuestion, Long> {
    List<ExamQuestion> findByTopic(ExamQuestionTopic topic);
    List<ExamQuestion> findByTopicFieldOfKnowledge(ExamQuestionFieldOfKnowledge fieldOfKnowledge);
}
