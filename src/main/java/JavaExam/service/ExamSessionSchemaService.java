package JavaExam.service;

import JavaExam.domain.ExamSessionSchema;
import JavaExam.domain.ExamSessionSchemaUnit;
import JavaExam.repository.ExamSessionSchemaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamSessionSchemaService {
    private final ExamSessionSchemaRepository schemaRepository;
    private final ExamSessionSchemaUnitService unitService;

    public ExamSessionSchemaService(ExamSessionSchemaRepository schemaRepository, ExamSessionSchemaUnitService unitService) {
        this.schemaRepository = schemaRepository;
        this.unitService = unitService;
    }

    public Optional<ExamSessionSchema> get(Long id) {
        return schemaRepository.findById(id);
    }

    public List<ExamSessionSchema> getAll() { return schemaRepository.findAll(); }

    public List<ExamSessionSchema> getAllSchemesWithIdAndNameOnly() {
        return schemaRepository.getAllWithIdAndNameOnly();
    }

    public List<ExamSessionSchema> getAllByNameIsNotNull() {
        return schemaRepository.getAllByNameIsNotNull();
    }

    public Optional<ExamSessionSchema> findByName(String name) {
        return schemaRepository.findByName(name);
    }

    public void save(ExamSessionSchema schema) {
        ExamSessionSchema savedSchema = schemaRepository.save(schema);
        List<ExamSessionSchemaUnit> units = schema.getUnits();
        for (ExamSessionSchemaUnit unit : units) {
            unit.setSessionSchema(savedSchema);
            unitService.save(unit);
        }
    }

    public boolean saveIfNameNotUsed(ExamSessionSchema schema) {
        String name = schema.getName();
        boolean isNamePresent = name != null && !name.isEmpty();
        boolean isNameWasNotUsed = isNamePresent && (!findByName(name).isPresent() || findByName(name).get().getId() == schema.getId());
        if (isNameWasNotUsed) {
            save(schema);
            return true;
        }
        return false;
    }
}
