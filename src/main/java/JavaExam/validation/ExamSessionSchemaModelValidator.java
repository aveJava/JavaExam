package JavaExam.validation;

import JavaExam.domain.ExamSessionSchema;
import JavaExam.model.ExamSessionSchemaModel;
import JavaExam.model.ExamSessionSchemaUnitModel;
import JavaExam.service.ExamSessionSchemaService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Optional;

@Component
public class ExamSessionSchemaModelValidator implements Validator {
    private final ExamSessionSchemaService schemaService;

    public ExamSessionSchemaModelValidator(ExamSessionSchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ExamSessionSchemaModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ExamSessionSchemaModel model = (ExamSessionSchemaModel) o;
        model.trim();   // убираются юниты с невыбранными foKn (разделами)

        // проверка имени схемы
        String name = model.getName();
        if (name == null || name.isEmpty()) errors.rejectValue("name", "", "Нужно ввести имя схемы");
        else {
            // если схема с таким именем уже есть в БД и их id не совпадают, или они сами не совпадают
            Optional<ExamSessionSchema> optional = schemaService.findByName(name);
            boolean isChanged = ExamSessionSchemaModel.isSchemaChanged(schemaService, model);
            if (optional.isPresent() && (optional.get().getId() != model.getId() || isChanged))
                errors.rejectValue("name", "", "Схема с таким именем уже есть в базе данных");
        }

        // проверка юнитов схемы
        List<ExamSessionSchemaUnitModel> units = model.getUnits();
        for (ExamSessionSchemaUnitModel unit : units) {
            unit.trim();    // убираются незаполненные пары тема-количество вопросов
        }
        if (units.size() < 1)   // проверка наличия хотя бы одного юнита
            errors.rejectValue("units", "", "Схема должна содержать хотя бы один заполненный юнит (раздел, тему и количество вопросов по ней)");
        else {                  // проверка наличия хотя бы одного топика (если он есть, значит есть и foKn)
            List<String> topics = units.get(0).getTopics();
            String topic = Optional.ofNullable(topics)
                    .filter(t -> t.size() > 0)
                    .map(t -> t.get(0))
                    .orElse(null);
            if (topic == null || topic.isEmpty()) {
                errors.rejectValue("units", "", "Схема должна содержать хотя бы одну тему и количество вопросов по ней");
            }
        }

        // проверка наличия количества вопросов для каждого выбранного топика
        for (int i = 0; i < model.getUnits().size(); i++) {
            ExamSessionSchemaUnitModel unit = model.getUnits().get(i);
            List<String> unitTopics = unit.getTopics();
            List<Integer> unitQuantityQuestions = unit.getQuantityQuestions();

            for (int j = 0; j < unitTopics.size(); j++) {
                if (unitTopics.get(j) != null && !unitTopics.get(j).isEmpty() &&
                        (unitQuantityQuestions.get(j) == null || unitQuantityQuestions.get(j) < 1)) {
                    errors.rejectValue("units", "", "Для каждой выбранной темы должно быть указано количество вопросов (и оно не должно быть отрицательным)");
                }
            }
        }

    }
}
