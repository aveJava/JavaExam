package JavaExam.repository;

import JavaExam.domain.ExamSessionSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamSessionSchemaRepository extends JpaRepository<ExamSessionSchema, Long> {
    @Transactional
    Optional<ExamSessionSchema> findByName(String name);
    @Query("SELECT new ExamSessionSchema(s.id, s.name) FROM ExamSessionSchema s WHERE s.name is not null")
    List<ExamSessionSchema> getAllSchemasWithIdAndNameOnly();
}
