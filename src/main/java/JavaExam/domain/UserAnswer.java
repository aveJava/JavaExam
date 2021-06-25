package JavaExam.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;

@Entity
@Table(name = "user_answer")
@EqualsAndHashCode(of = "id")
@Getter @Setter
@DynamicUpdate
@DynamicInsert
@SelectBeforeUpdate
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private ExamSession session;

    @OneToOne
    @JoinColumn(name = "question_id")
    private ExamQuestion question;

    @Column(name = "selected_answer_number")
    private byte selectedAnswerNumber;

    public boolean isCorrect() {
        return  selectedAnswerNumber == question.getCorrectAnswerNumber();
    }

}
