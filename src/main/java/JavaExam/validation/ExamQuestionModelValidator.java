package JavaExam.validation;

import JavaExam.domain.ExamQuestionFieldOfKnowledge;
import JavaExam.domain.ExamQuestionTopic;
import JavaExam.model.ExamQuestionModel;
import JavaExam.service.ExamQuestionFieldOfKnowledgeService;
import JavaExam.service.ExamQuestionService;
import JavaExam.service.ExamQuestionTopicService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class ExamQuestionModelValidator implements Validator {
    private final ExamQuestionFieldOfKnowledgeService fieldOfKnowledgeService;
    private final ExamQuestionTopicService topicService;

    public ExamQuestionModelValidator(ExamQuestionFieldOfKnowledgeService fieldOfKnowledgeService, ExamQuestionTopicService topicService) {
        this.fieldOfKnowledgeService = fieldOfKnowledgeService;
        this.topicService = topicService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ExamQuestionModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ExamQuestionModel model = (ExamQuestionModel) o;

        ExamQuestionFieldOfKnowledge field = fieldOfKnowledgeService.getByName(model.getFieldOfKnowledge());
        ExamQuestionTopic topic = topicService.findByFoKnNameAndName(model.getFieldOfKnowledge(), model.getTopic());

        if (field == null) errors.rejectValue("fieldOfKnowledge", "", "Нет такого раздела");
        else if (topic == null) errors.rejectValue("topic","", "В указанном разделе нет такой темы");
        if (model.getQuestion() == null || model.getQuestion().isEmpty())
            errors.rejectValue("question", "","Нужно написать вопрос");
        if (model.getAnswer1() == null || model.getAnswer1().isEmpty() ||
            model.getAnswer2() == null || model.getAnswer2().isEmpty())
            errors.rejectValue("answer1","", "Обязательно должно быть по крайней мере два ответа");
        String correctNum = model.getCorrectAnswerNumber();
        if (correctNum == null || !correctNum.matches("^\\d{1,2}$")) {
            errors.rejectValue("correctAnswerNumber", "", "Некорректно введен номер правльного ответа");
        }
        else if (Integer.parseInt(correctNum) > 4) errors.rejectValue("correctAnswerNumber", "", "Слишком большой номер правильного ответа");
        else {
            int corrNum = Integer.parseInt(correctNum);
            List<String> list = List.of(model.getAnswer1(), model.getAnswer2(), model.getAnswer3(), model.getAnswer4());
            for (int i = 0; i < corrNum - 1; i++) {
                if (list.get(i) == null || list.get(i).isEmpty())
                    errors.rejectValue("correctAnswerNumber", "", "Не должно быть пустых ответов с номером меньше, чем номер правильного ответа");
            }
        }
    }
}
