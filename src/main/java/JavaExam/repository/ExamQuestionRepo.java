package JavaExam.repository;

import JavaExam.domain.ExamQuestion;
import JavaExam.domain.ExamQuestionFieldOfKnowledge;
import JavaExam.domain.ExamQuestionTopic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamQuestionRepo extends JpaRepository<ExamQuestion, Long> {
    List<ExamQuestion> findByTopic(ExamQuestionTopic topic);
    List<ExamQuestion> findByTopicFieldOfKnowledge(ExamQuestionFieldOfKnowledge fieldOfKnowledge);
    Page<ExamQuestion> findByTopicFieldOfKnowledgeName(String foKnName, Pageable pageable);
    Page<ExamQuestion> findByTopicFieldOfKnowledgeNameAndTopicName(String foKnName, String topicName, Pageable pageable);

    @Query(value="SELECT * FROM exam_question q WHERE q.topic_id = :topicId ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<ExamQuestion> findRandomQuestionsByTopic(@Param("topicId") Long topicId, @Param("limit") Integer limit);

    int countByTopic(ExamQuestionTopic topic);
}
