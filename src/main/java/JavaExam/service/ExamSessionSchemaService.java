package JavaExam.service;

import JavaExam.domain.ExamSessionSchema;
import JavaExam.repository.ExamSessionSchemaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExamSessionSchemaService {
    private final ExamSessionSchemaRepository schemaRepository;

    public ExamSessionSchemaService(ExamSessionSchemaRepository schemaRepository) {
        this.schemaRepository = schemaRepository;
    }

    public Optional<ExamSessionSchema> get(Long id) {
        return schemaRepository.findById(id);
    }
}
