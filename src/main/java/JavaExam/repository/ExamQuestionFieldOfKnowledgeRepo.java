package JavaExam.repository;

import JavaExam.domain.ExamQuestionFieldOfKnowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamQuestionFieldOfKnowledgeRepo extends JpaRepository<ExamQuestionFieldOfKnowledge, Long> {
    ExamQuestionFieldOfKnowledge findByName(String name);
}
