package JavaExam.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "exam_session")
@EqualsAndHashCode(of = "id")
@Getter @Setter
@DynamicUpdate
@DynamicInsert
@SelectBeforeUpdate
public class ExamSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    @Convert(converter = SessionDateConverter.class)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "session", fetch = FetchType.EAGER)
    private List<UserAnswer> answers;

    @Column(name = "completed")
    private boolean isCompleted;


    // возвращает  Map<Map.Entry<'раздел', 'доля правильных ответов в этом разделе'> Map<'тема этого раздела', 'доля правильных ответов по этой теме'>>
    public Map<Map.Entry<ExamQuestionFieldOfKnowledge, Double>, Map<ExamQuestionTopic, Double>> getSessionResult() {
        // получение карты сессии
        ExamSessionSchema schema = user.getSessionSchema();
        Map<ExamQuestionFieldOfKnowledge, Map<ExamQuestionTopic, Integer>> schemaMap = schema.getSessionSchemaMap();

        // получение карты правильных ответов
        Map<ExamQuestionFieldOfKnowledge, Map<ExamQuestionTopic, Integer>> correctAnswersMap = getCorrectAnswersMap();

        // получение карты 'тема - доля правильных ответов' (доля находится как 'кол-во_правильных_ответов / общее_кол-во_вопросов')
        Map<ExamQuestionTopic, Double> topicMap = new HashMap<>();
        for (ExamQuestionFieldOfKnowledge foKn : schemaMap.keySet()) {
            Map<ExamQuestionTopic, Integer> thisFoKnMapInSessionScheme = schemaMap.get(foKn);
            Map<ExamQuestionTopic, Integer> thisFoKnMapInCorrectAnswers = correctAnswersMap.get(foKn);

            for (ExamQuestionTopic topic : thisFoKnMapInSessionScheme.keySet()) {
                Integer countOfQuestionsAsked = thisFoKnMapInSessionScheme.get(topic);
                Integer countCorrectAnswers = Optional
                        .ofNullable(thisFoKnMapInCorrectAnswers)
                        .map(foKn1 -> foKn1.get(topic))
                        .orElse(0);
                topicMap.put(topic, countCorrectAnswers * 1.0 / countOfQuestionsAsked);
            }
        }

        // получение карты 'раздел - доля правильных ответов'
        Map<ExamQuestionFieldOfKnowledge, Double> foKnMap = new HashMap<>(schemaMap.size());
        for (ExamQuestionFieldOfKnowledge foKn : schemaMap.keySet()) {
            if (!correctAnswersMap.containsKey(foKn)) {
                foKnMap.put(foKn, 0.0);
                continue;
            }

            Double totalScore  = topicMap.entrySet().stream()
                    .filter(entry -> entry.getKey().getFieldOfKnowledge() == foKn)
                    .map((entry) -> entry.getValue())
                    .reduce((a, b) -> a + b)
                    .get();
            Double averageScore = totalScore / schemaMap.get(foKn).size();
            foKnMap.put(foKn, averageScore);
        }

        // составление итоговой карты
        Map<Map.Entry<ExamQuestionFieldOfKnowledge, Double>, Map<ExamQuestionTopic, Double>> map = new HashMap<>();
        foKnMap.entrySet().stream()
                .forEach(entry -> map.put(
                        Map.entry(entry.getKey(), entry.getValue()),
                        // topicMap в которой оставлены entry только с темами по текущему foKn
                        topicMap.entrySet().stream()
                                .filter(en -> en.getKey().getFieldOfKnowledge() == entry.getKey())
                                .collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue()))));

        return map;
    }

    // возвращает  Map<'раздел', Map<'тема этого раздела', 'количество правильных ответов по этой теме'>>
    public Map<ExamQuestionFieldOfKnowledge, Map<ExamQuestionTopic, Integer>> getCorrectAnswersMap() {
        Map<ExamQuestionFieldOfKnowledge, Map<ExamQuestionTopic, Integer>> map = new HashMap<>();

        for (UserAnswer answer : answers) {
            if (answer.isCorrect()) {
                ExamQuestionTopic answerTopic = answer.getQuestion().getTopic();
                ExamQuestionFieldOfKnowledge answerFoKn = answerTopic.getFieldOfKnowledge();

                if (!map.containsKey(answerFoKn)) map.put(answerFoKn, new HashMap<>());
                Map<ExamQuestionTopic, Integer> currentFoKnMap = map.get(answerFoKn);
                Integer counterOfCorrectAnswersOnTopicThisQuestion = currentFoKnMap.containsKey(answerTopic) ?
                        currentFoKnMap.get(answerTopic) : 0;
                currentFoKnMap.put(answerTopic, ++counterOfCorrectAnswersOnTopicThisQuestion);
            }
        }

        return map;
    }

    public Map<ExamQuestionFieldOfKnowledge, Map<ExamQuestionTopic, List<UserAnswer>>> getAnswerMap() {
        Map<ExamQuestionFieldOfKnowledge, Map<ExamQuestionTopic, List<UserAnswer>>> map = new HashMap<>();

        for (UserAnswer answer : answers) {
            ExamQuestionTopic answerTopic = answer.getQuestion().getTopic();
            ExamQuestionFieldOfKnowledge answerFoKn = answerTopic.getFieldOfKnowledge();

            Map<ExamQuestionTopic, List<UserAnswer>> foKnMap = map.get(answerFoKn);
            if (foKnMap == null) {
                var newFoKnMap = new HashMap<ExamQuestionTopic, List<UserAnswer>>();
                map.put(answerFoKn, foKnMap = newFoKnMap);
            }

            List<UserAnswer> thisTopicAnswers = foKnMap.get(answerTopic);
            if (thisTopicAnswers == null) {
                foKnMap.put(answerTopic, thisTopicAnswers = new ArrayList<>());
            }

            thisTopicAnswers.add(answer);
        }

        return map;
    }

}

@Converter
class SessionDateConverter implements AttributeConverter<LocalDate, Date> {
    @Override
    public java.sql.Date convertToDatabaseColumn(LocalDate entityValue) {
        return java.sql.Date.valueOf(entityValue);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date databaseValue) {
        return databaseValue.toLocalDate();
    }
}
