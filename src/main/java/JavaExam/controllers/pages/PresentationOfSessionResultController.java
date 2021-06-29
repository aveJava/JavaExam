package JavaExam.controllers.pages;

import JavaExam.domain.*;
import JavaExam.service.ExamQuestionTopicService;
import JavaExam.service.ExamSessionService;
import JavaExam.utils.Numbering;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/presentation_of_session_results")
public class PresentationOfSessionResultController {
    private final ExamSessionService sessionService;
    private final ExamQuestionTopicService topicService;

    // данные о сессии
    private Long id;
    private ExamSession session;
    private Double finalScore;
    private Map<Map.Entry<ExamQuestionFieldOfKnowledge, Double>, Map<ExamQuestionTopic, Double>> result;


    public PresentationOfSessionResultController(ExamSessionService sessionService, ExamQuestionTopicService topicService) {
        this.sessionService = sessionService;
        this.topicService = topicService;
    }

    @GetMapping("/{id}")
    @Transactional
    public String displayPage(@PathVariable("id") Long id, Model model,
                              @RequestParam(value = "mod", required = false) String mod,
                              @RequestParam(value = "topicId", required = false) Long topicId) {
        if (this.id != id) {
            if (!checkIdAndLoadSessionOrElsePrepareModelForRedirect(id, model)) return "redirect:/presentation_of_session_results/presentation_of_session_results-page";
        }

        switch (mod) {
            case "question_and_answer":
                questionAndAnswerMod(model);
                break;
            case "topic":
                topicMod(model, topicId);
                break;
            default:
                diagramMod(model);
        }

        return "presentation_of_session_results/presentation_of_session_results-page";
    }

    private void diagramMod(Model model) {
        addUserAndDateAndFinalScoreInModel(model);
        model.addAttribute("mod", "diagram");
        model.addAttribute("result_map", result);
        model.addAttribute("sessionId", id);
    }

    private void questionAndAnswerMod(Model model) {
        addUserAndDateAndFinalScoreInModel(model);
        model.addAttribute("mod", "questionAndAnswer");
        model.addAttribute("AnswerMap", session.getAnswerMap());
        model.addAttribute("num", new Numbering());
    }

    private void topicMod(Model model, Long topicId) {
        ExamQuestionTopic topic = topicService.get(topicId);
        if (topic == null) {
            model.addAttribute("mess", "Нет такой темы!");
            return;
        }
        List<UserAnswer> answers = session.getAnswersByTopic(topic);
        if (answers.size() == 0) {
            model.addAttribute("mess", "По этой теме не было дано ни одного ответа!");
            return;
        }

        addUserAndDateAndFinalScoreInModel(model);
        model.addAttribute("mod", "topic");
        model.addAttribute("answers", session.getAnswersByTopic(topic));
    }

    private void addUserAndDateAndFinalScoreInModel(Model model) {
        model.addAttribute("user", session.getUser());
        model.addAttribute("date", session.getDate());
        model.addAttribute("month", session.getDate().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()));
        model.addAttribute("finalScore", finalScore);
    }

    // проверяет если для указанного id пройденная сессия, если есть, загружает данные о сессии в поля класса и возвращает true
    // если нет - подготавливает модель для редиректа и возвращает false
    private boolean checkIdAndLoadSessionOrElsePrepareModelForRedirect(Long id, Model model) {
        ExamSession session;

        // проверка аргументов
        if (id == null || id < 0 || (session = sessionService.get(id)) == null) {
            model.addAttribute("mess", "Нет такой сессии.");
            return false;
        }
        if (!session.isCompleted()) {
            model.addAttribute("mess", "Эта сессия еще не сдана.");
            return false;
        }

        // инициализация полей
        this.id = id;
        this.session = session;
        result = session.getSessionResult();
        finalScore = result.entrySet().stream()
                .map(en -> en.getKey().getValue())
                .reduce(Double::sum)
                .orElse(0.) / result.size() * 100;

        return true;
    }
}
