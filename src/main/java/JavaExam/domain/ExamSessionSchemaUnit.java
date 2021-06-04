package JavaExam.domain;

import JavaExam.model.ExamSessionSchemaUnitModel;
import JavaExam.service.ExamQuestionTopicService;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "session_schema_unit")
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@SelectBeforeUpdate
public class ExamSessionSchemaUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_schema")
    private ExamSessionSchema sessionSchema;

    // Раздел
    @ManyToOne
    @JoinColumn(name = "field_of_knowledge")
    private ExamQuestionFieldOfKnowledge foKn;

    // Темы
    @Convert(converter = ExamSessionSchemaTopicsConverter.class)
    private List<ExamQuestionTopic> topics;

    // Количества вопросов на каждую тему
    @Convert(converter = ExamSessionSchemaTopicsQuantityQuestionsConverter.class)
    private List<Integer> quantityQuestions;

    public ExamSessionSchemaUnitModel toModel() {
        ExamSessionSchemaUnitModel model = new ExamSessionSchemaUnitModel();

        if (sessionSchema != null) model.setSessionSchemaId(sessionSchema.getId());
        if (foKn != null) model.setFoKn(foKn.getName());
        if (topics != null) {
            List<String> topicsListStr = new ArrayList<>(topics.size());
            for (int i = 0; i < topics.size(); i++) {
                String topic = topics.get(i) == null ? null : topics.get(i).getName();
                topicsListStr.add(topic);
            }
            model.setTopics(topicsListStr);
        }
        if (quantityQuestions != null) {
            List<Integer> quantityListStr = new ArrayList<>(quantityQuestions.size());
            for (int i = 0; i < quantityQuestions.size(); i++) {
                quantityListStr.add(quantityQuestions.get(i));
            }
            model.setQuantityQuestions(quantityListStr);
        }

        return model;
    }

}

@Converter
final class ExamSessionSchemaTopicsConverter implements AttributeConverter<List<ExamQuestionTopic>, String> {
    private static final String SEPARATOR = "※";

    @Autowired
    private ApplicationContext context;

    @Override
    public String convertToDatabaseColumn(List<ExamQuestionTopic> examQuestionTopics) {
        String topics = examQuestionTopics.stream()
                .map(topic -> String.valueOf(topic.getId()))
                .collect(Collectors.joining(SEPARATOR));
        return topics;
    }

    @Override
    public List<ExamQuestionTopic> convertToEntityAttribute(String s) {
        String[] topicsId = s.split(SEPARATOR);
        List<ExamQuestionTopic> topicList = Arrays.stream(topicsId)
                .map(str -> context.getBean(ExamQuestionTopicService.class).get(Long.parseLong(str)))
                .collect(Collectors.toList());
        return topicList;
    }
}

@Converter
final class ExamSessionSchemaTopicsQuantityQuestionsConverter implements AttributeConverter<List<Integer>, String> {
    private static final String SEPARATOR = "※";

    @Override
    public String convertToDatabaseColumn(List<Integer> integers) {
        String numbers = integers.stream().map(i -> String.valueOf(i)).collect(Collectors.joining(SEPARATOR));
        return numbers;
    }

    @Override
    public List<Integer> convertToEntityAttribute(String s) {
        String[] numbers = s.split(SEPARATOR);
        List<Integer> list = Arrays.stream(numbers).map(num -> Integer.parseInt(num)).collect(Collectors.toList());
        return list;
    }
}
