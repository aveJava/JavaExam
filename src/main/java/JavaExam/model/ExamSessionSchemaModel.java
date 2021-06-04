package JavaExam.model;

import JavaExam.domain.ExamQuestionTopic;
import JavaExam.domain.ExamSessionSchema;
import JavaExam.domain.ExamSessionSchemaUnit;
import JavaExam.service.ExamQuestionFieldOfKnowledgeService;
import JavaExam.service.ExamQuestionTopicService;
import JavaExam.service.ExamSessionSchemaService;
import JavaExam.service.ExamSessionSchemaUnitService;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExamSessionSchemaModel {
    private Long id;
    private String name;
    private List<ExamSessionSchemaUnitModel> units;

    public void trim() {
        for (int i=0; i<units.size(); i++) {
            String foKn = units.get(i).getFoKn();
            if (foKn == null || foKn.isEmpty()) {
                units.remove(i);
                i--;
            }
        }
    }

    public ExamSessionSchema toEntity(ExamSessionSchemaUnitService unitService, ExamSessionSchemaService schemaService,
                                      ExamQuestionFieldOfKnowledgeService foKnService, ExamQuestionTopicService topicService) {
        ExamSessionSchema entity = new ExamSessionSchema();

        entity.setId(id);
        entity.setName(name);
        // конвертация юнитов
        List<ExamSessionSchemaUnit> units = new ArrayList<>(this.units.size());
        for (int i = 0; i < this.units.size(); i++) {
            ExamSessionSchemaUnitModel thisUnitModel = this.units.get(i);   // юнит-модель текущей итерации
            ExamSessionSchemaUnit unit = new ExamSessionSchemaUnit();       // юнит текущей итерации

            //unit.setSessionSchema(schemaService.get(id).get());             // установка схемы сессии, к которой принадлежит данный юнит
            unit.setFoKn(foKnService.getByName(thisUnitModel.getFoKn()));   // установка раздела юнита

            // конвертация топиков юнита
            List<String> topicsStr = thisUnitModel.getTopics();
            List<ExamQuestionTopic> topics = new ArrayList<>(topicsStr.size());
            for (int j = 0; j < topicsStr.size(); j++) {
                String foKnName = thisUnitModel.getFoKn();
                String topicName = thisUnitModel.getTopics().get(j);
                topics.add(topicService.findByFoKnNameAndName(foKnName, topicName));
            }
            unit.setTopics(topics);
            unit.setQuantityQuestions(thisUnitModel.getQuantityQuestions());

            units.add(unit);
        }
        entity.setUnits(units);

        return entity;
    }
}
