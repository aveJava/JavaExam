package JavaExam.controllers.pages;

import JavaExam.enums.EditingTab;
import JavaExam.model.ExamQuestionModel;
import JavaExam.service.ExamQuestionFieldOfKnowledgeService;
import JavaExam.service.ExamQuestionService;
import JavaExam.service.ExamQuestionTopicService;
import JavaExam.utils.Numbering;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/editing_tests")
public class EditingTestsController {
    private final ExamQuestionFieldOfKnowledgeService fieldOfKnowledgeService;
    private final ExamQuestionTopicService topicService;
    private final ExamQuestionService questionService;

    private EditingTab tab;

    public EditingTestsController(ExamQuestionFieldOfKnowledgeService fieldOfKnowledgeService, ExamQuestionTopicService topicService, ExamQuestionService questionService) {
        this.fieldOfKnowledgeService = fieldOfKnowledgeService;
        this.topicService = topicService;
        this.questionService = questionService;

        tab = EditingTab.TOPICS;
    }

    @GetMapping
    public String displayPage(Model model) {
        model.addAttribute("tab", tab);
        switch (tab) {
            case TOPICS:
                model.addAttribute("fieldsOfKnowledge", fieldOfKnowledgeService.getAll());
                model.addAttribute("topics", topicService.getAll());
                model.addAttribute("numbering", new Numbering());
                break;
            case NEW_QUESTION:
                if (!model.containsAttribute("model")) {
                    model.addAttribute("model", new ExamQuestionModel());
                }
                model.addAttribute("fieldsOfKnowledge", fieldOfKnowledgeService.getAll());
                model.addAttribute("topics", topicService.getAll());
                break;
            case VIEW_QUESTIONS:
                break;
        }


        return "editing_tests/editing_tests-page";
    }

    @GetMapping("/switch")
    public String switchTab(@RequestParam("tab") EditingTab newTab) {
        switch (newTab) {
            case TOPICS:
                tab = EditingTab.TOPICS;
                break;
            case NEW_QUESTION:
                tab = EditingTab.NEW_QUESTION;
                break;
            case VIEW_QUESTIONS:
                tab = EditingTab.VIEW_QUESTIONS;
                break;
        }
        return "redirect:/editing_tests";
    }
}
