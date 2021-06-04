package JavaExam.service;

import JavaExam.domain.ExamSessionSchemaUnit;
import JavaExam.repository.ExamSessionSchemaUnitRepository;
import org.springframework.stereotype.Service;

@Service
public class ExamSessionSchemaUnitService {
    private final ExamSessionSchemaUnitRepository unitRepository;

    public ExamSessionSchemaUnitService(ExamSessionSchemaUnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public void save(ExamSessionSchemaUnit unit) {
        unitRepository.save(unit);
    }
}
