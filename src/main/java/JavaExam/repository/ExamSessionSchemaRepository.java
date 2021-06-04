package JavaExam.repository;

import JavaExam.domain.ExamSessionSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamSessionSchemaRepository extends JpaRepository<ExamSessionSchema, Long> {
    Optional<ExamSessionSchema> findByName(String name);
}
