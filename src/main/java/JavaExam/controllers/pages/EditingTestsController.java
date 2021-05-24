package JavaExam.controllers.pages;

import JavaExam.enums.EditingTab;
import JavaExam.service.ExamQuestionFieldOfKnowledgeService;
import JavaExam.service.ExamQuestionService;
import JavaExam.service.ExamQuestionTopicService;
import JavaExam.utils.Numbering;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        model.addAttribute("fieldsOfKnowledge", fieldOfKnowledgeService.getAll());
        model.addAttribute("topics", topicService.getAll());
        model.addAttribute("numbering", new Numbering());


        return "editing_tests/editing_tests-page";
    }
}
