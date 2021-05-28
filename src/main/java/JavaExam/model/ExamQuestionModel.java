package JavaExam.model;

import JavaExam.domain.ExamQuestion;
import JavaExam.service.ExamQuestionTopicService;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExamQuestionModel {
    private Long id;
    private String fieldOfKnowledge;
    private String topic;
    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String correctAnswerNumber;

    public ExamQuestion toEntity(ExamQuestionTopicService topicService) {
        ExamQuestion entity = new ExamQuestion();
        entity.setId(id);
        entity.setTopic(topicService.findByFoKnNameAndName(fieldOfKnowledge, topic));
        entity.setQuestion(question);
        entity.setAnswers(List.of(answer1, answer2, answer3, answer4));
        entity.setCorrectAnswerNumber((byte) Integer.parseInt(correctAnswerNumber));

        return entity;
    }
}
