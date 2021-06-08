package JavaExam.controllers.pages;

import JavaExam.domain.ExamQuestion;
import JavaExam.enums.EditingTab;
import JavaExam.model.ExamQuestionModel;
import JavaExam.service.ExamQuestionFieldOfKnowledgeService;
import JavaExam.service.ExamQuestionService;
import JavaExam.service.ExamQuestionTopicService;
import JavaExam.utils.Numbering;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/editing_tests")
public class EditingTestsController {
    private final ExamQuestionFieldOfKnowledgeService fieldOfKnowledgeService;
    private final ExamQuestionTopicService topicService;
    private final ExamQuestionService questionService;

    private EditingTab tab;
    private String foKnName;
    private String topicName;
    private int pageNumber;
    private int maxPage;
    private long totalElements;
    private int pageSize;

    public EditingTestsController(ExamQuestionFieldOfKnowledgeService fieldOfKnowledgeService, ExamQuestionTopicService topicService, ExamQuestionService questionService) {
        this.fieldOfKnowledgeService = fieldOfKnowledgeService;
        this.topicService = topicService;
        this.questionService = questionService;

        tab = EditingTab.TOPICS;
        this.pageNumber = 1;
        this.pageSize = 10;
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
                if (model.containsAttribute("editing")) {
                    model.addAttribute("fieldsOfKnowledge", fieldOfKnowledgeService.getAll());
                    model.addAttribute("topics", topicService.getAll());
                } else {
                    Page<ExamQuestion> page = questionService.search(foKnName, topicName, pageNumber, pageSize);
                    maxPage = page.getTotalPages();
                    totalElements = page.getTotalElements();
                    model.addAttribute("page", page);
                    model.addAttribute("thisPage", pageNumber);
                    model.addAttribute("maxPage", maxPage);
                    model.addAttribute("totalElements", totalElements);
                }
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

    // слушает кнопки toolbar'а, перелистывает страницу результатов или меняет ее размер
    @GetMapping("/toolbar/{button}")
    public String toolbar(@PathVariable("button") String button,
                          @RequestParam(value = "title", required = false) String title,
                          @RequestParam(value = "size", required = false) Integer size) {

        if (button == null) return "";

        if (button.equals("NumberButtons")) {
            switch (title) {
                case "<<":
                    pageNumber = 1;
                    break;
                case "<":
                    if (pageNumber > 1) pageNumber--;
                    break;
                case ">":
                    pageNumber++;
                    break;
                case ">>":
                    pageNumber = maxPage;
                    break;
            }
        }

        if (button.equals("PageSize")) {
            pageSize = size;
        }

        return "redirect:/editing_tests";
    }
}
