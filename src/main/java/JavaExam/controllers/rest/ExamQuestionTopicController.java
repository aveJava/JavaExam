package JavaExam.controllers.rest;

import JavaExam.domain.ExamQuestionFieldOfKnowledge;
import JavaExam.domain.ExamQuestionTopic;
import JavaExam.service.ExamQuestionFieldOfKnowledgeService;
import JavaExam.service.ExamQuestionTopicService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/entities/exam_question_topics")
public class ExamQuestionTopicController {
    private final ExamQuestionTopicService topicService;
    private final ExamQuestionFieldOfKnowledgeService fieldOfKnowledgeService;

    public ExamQuestionTopicController(ExamQuestionTopicService topicService,
                                       ExamQuestionFieldOfKnowledgeService fieldOfKnowledgeService) {
        this.topicService = topicService;
        this.fieldOfKnowledgeService = fieldOfKnowledgeService;
    }

    @PostMapping
    public String create(@RequestParam("foKn") String foKn, @RequestParam("name") String name) {
        boolean isFoKnPresent = foKn != null && !foKn.isEmpty();
        boolean isNamePresent = name != null && !name.isEmpty();

        if (isFoKnPresent && isNamePresent) {
            ExamQuestionFieldOfKnowledge fieldOfKnowledge = fieldOfKnowledgeService.getByName(foKn);
            if (fieldOfKnowledge != null) {
                boolean isPresentInDB = topicService.hasFindByFieldOfKnowledgeAndFindByName(fieldOfKnowledge, name);
                if (!isPresentInDB) {
                    ExamQuestionTopic topic = new ExamQuestionTopic();
                    topic.setFieldOfKnowledge(fieldOfKnowledge);
                    topic.setName(name);
                    topicService.save(topic);
                }
            }
        }

        return "redirect:/editing_tests";
    }

    @PatchMapping
    public String update(@RequestParam("number") String number, @RequestParam("foKn") String foKn, @RequestParam("name") String name) {
        // валидация данных формы
        boolean isNumberPresent = number != null && !number.isEmpty() && !number.matches("\\D") && number.length() <= 10;
        boolean isFoKnPresent = foKn != null && !foKn.equals("Использовать старый");
        boolean isNamePresent = name != null && !name.isEmpty();
        if (!isNumberPresent || !(isFoKnPresent || isNamePresent)) return "redirect:/editing_tests";

        int num;
        try {
            num = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return "redirect:/editing_tests";
        }

        // получение редактируемого топика
        List<ExamQuestionTopic> list = topicService.getAll();
        if (num < 0 || num > list.size()) return "redirect:/editing_tests";
        ExamQuestionTopic topic = list.get(num - 1);

        // изменение и сохранение редактируемого топика
        if (isFoKnPresent) {
            ExamQuestionFieldOfKnowledge fieldOfKnowledge = fieldOfKnowledgeService.getByName(foKn);
            if (fieldOfKnowledge != null) topic.setFieldOfKnowledge(fieldOfKnowledgeService.getByName(foKn));
        }
        if (isNamePresent) topic.setName(name);
        topicService.save(topic);

        return "redirect:/editing_tests";
    }

    @DeleteMapping
    public String delete(@RequestParam("number") String number) {
        if (number == null || number.isEmpty() || number.matches("\\D") || number.length() > 10) return "redirect:/editing_tests";

        int num;
        try {
            num = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return "redirect:/editing_tests";
        }

        List<ExamQuestionTopic> list = topicService.getAll();
        if (num < 0 || num > list.size()) return "redirect:/editing_tests";
        ExamQuestionTopic topic = list.get(num - 1);
        topicService.delete(topic);

        return "redirect:/editing_tests";
    }
}
