package JavaExam.repository;

import JavaExam.domain.ExamSessionSchemaUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamSessionSchemaUnitRepository extends JpaRepository<ExamSessionSchemaUnit, Long> {
}
