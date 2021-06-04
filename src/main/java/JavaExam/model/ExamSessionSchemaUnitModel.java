package JavaExam.model;

import JavaExam.domain.ExamQuestionTopic;
import JavaExam.domain.ExamSessionSchemaUnit;
import JavaExam.service.ExamQuestionFieldOfKnowledgeService;
import JavaExam.service.ExamQuestionTopicService;
import JavaExam.service.ExamSessionSchemaService;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExamSessionSchemaUnitModel {
    private Long id;

    // Id схемы, к которой относится данный юнит
    private Long sessionSchemaId;

    // Раздел
    private String foKn;

    // Темы
    private List<String> topics;

    // Количества вопросов на каждую тему
    private List<Integer> quantityQuestions;

    public ExamSessionSchemaUnit toEntity(ExamSessionSchemaService schemaService, ExamQuestionFieldOfKnowledgeService foKnService, ExamQuestionTopicService topicService) {
        ExamSessionSchemaUnit entity = new ExamSessionSchemaUnit();

        entity.setId(id);
        entity.setSessionSchema(schemaService.get(sessionSchemaId).get());
        entity.setFoKn(foKnService.getByName(foKn));
        List<ExamQuestionTopic> topicsList = new ArrayList<>(topics.size());
        for (int i = 0; i < topics.size(); i++) {
            topicsList.add(topicService.findByFoKnNameAndName(foKn, topics.get(i)));
        }
        entity.setQuantityQuestions(quantityQuestions);

        return entity;
    }

    // убирает все пустые пары тема-число вопросов (используется перед валидацией формы)
    public void trim() {
        if (topics != null && quantityQuestions != null) {
            int length = Math.min(topics.size(), quantityQuestions.size());
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    boolean isTopicsEmpty = topics.get(i) == null || topics.get(i).isEmpty();
                    boolean isQuantityEmpty = quantityQuestions.get(i) == null || quantityQuestions.get(i) < 1;
                    if (isTopicsEmpty && isQuantityEmpty) {
                        topics.remove(i);
                        quantityQuestions.remove(i);
                        i--; length--;
                    }
                }
            }
        }
    }
}
