package JavaExam.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "exam_question")
@EqualsAndHashCode(of = "id")
@Getter @Setter
@DynamicUpdate
@DynamicInsert
@SelectBeforeUpdate
public class ExamQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private ExamQuestionTopic topic;

    @Column(name = "question")
    private String question;

    @Column(name = "answers")
    @Convert(converter = AnswersConverter.class)
    private List<String> answers;

    @Column(name = "correct_answer_number")
    private byte correctAnswerNumber;

}

@Converter
class AnswersConverter implements AttributeConverter<List<String>, String> {

    private static final String SEPARATOR = "※";

    @Override
    public String convertToDatabaseColumn(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) sb.append(SEPARATOR);
            if (list.get(i) != null) sb.append(list.get(i));
        }

        return sb.toString();
    }

    @Override
    public List<String> convertToEntityAttribute(String dbList) {
        return List.of(dbList.split(SEPARATOR));
    }

}