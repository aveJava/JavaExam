package JavaExam.controllers.rest;

import JavaExam.domain.ExamQuestionFieldOfKnowledge;
import JavaExam.service.ExamQuestionFieldOfKnowledgeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/entities/exam_question_field_of_knowledge")
public class ExamQuestionFieldOfKnowledgeController {
    private final ExamQuestionFieldOfKnowledgeService fieldOfKnowledgeService;

    public ExamQuestionFieldOfKnowledgeController(ExamQuestionFieldOfKnowledgeService fieldOfKnowledgeService) {
        this.fieldOfKnowledgeService = fieldOfKnowledgeService;
    }

    @PostMapping
    public String create(@RequestParam("name") String name) {
        if (name != null && !name.isEmpty()) {
            ExamQuestionFieldOfKnowledge fieldOfKnowledge = new ExamQuestionFieldOfKnowledge();
            fieldOfKnowledge.setName(name);
            fieldOfKnowledgeService.save(fieldOfKnowledge);
        }

        return "redirect:/admin/editing_tests";
    }

    @PatchMapping
    public String update(@RequestParam("number") String number, @RequestParam("name") String name) {
        if (number == null || name == null || number.isEmpty() || name.isEmpty() || number.matches("\\D") || number.length() > 10) return "redirect:/admin/editing_tests";

        int num;
        try {
            num = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return "redirect:/admin/editing_tests";
        }

        List<ExamQuestionFieldOfKnowledge> list = fieldOfKnowledgeService.getAll();
        if (num < 0 || num > list.size()) return "redirect:/admin/editing_tests";
        ExamQuestionFieldOfKnowledge fieldOfKnowledge = list.get(num - 1);
        fieldOfKnowledge.setName(name);
        fieldOfKnowledgeService.save(fieldOfKnowledge);

        return "redirect:/admin/editing_tests";
    }

    @DeleteMapping
    public String delete(@RequestParam("number") String number) {
        if (number == null || number.isEmpty() || number.matches("\\D") || number.length() > 10) return "redirect:/admin/editing_tests";

        int num;
        try {
            num = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return "redirect:/admin/editing_tests";
        }

        List<ExamQuestionFieldOfKnowledge> list = fieldOfKnowledgeService.getAll();
        if (num < 0 || num > list.size()) return "redirect:/admin/editing_tests";
        ExamQuestionFieldOfKnowledge fieldOfKnowledge = list.get(num - 1);
        fieldOfKnowledgeService.delete(fieldOfKnowledge);

        return "redirect:/admin/editing_tests";
    }
}
