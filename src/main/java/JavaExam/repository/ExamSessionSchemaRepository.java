package JavaExam.repository;

import JavaExam.domain.ExamSessionSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamSessionSchemaRepository extends JpaRepository<ExamSessionSchema, Long> {
}
