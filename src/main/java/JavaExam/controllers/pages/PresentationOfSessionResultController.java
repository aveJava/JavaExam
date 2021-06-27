package JavaExam.controllers.pages;

import JavaExam.domain.ExamSession;
import JavaExam.service.ExamSessionService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.TextStyle;
import java.util.Locale;

@Controller
@RequestMapping("/display_session_result")
public class PresentationOfSessionResultController {
    private final ExamSessionService sessionService;

    public PresentationOfSessionResultController(ExamSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/{id}")
    @Transactional
    public String displayPage(@PathVariable("id") Long id, Model model) {
        // проверка аргументов метода
        if (id == null || id < 0) {
            model.addAttribute("mess", "Нет такой сессии.");
            return "presentation_of_session_results/presentation_of_session_results-page";
        }
        ExamSession session = sessionService.get(id);
        if (session == null || !session.isCompleted()) {
            model.addAttribute("mess", "Эта сессия еще не сдана.");
            return "presentation_of_session_results/presentation_of_session_results-page";
        }

        // карта результатов сессии и итоговый балл (процент выполнения теста)
        var result = session.getSessionResult();
        Double finalScore = result.entrySet().stream().map(en -> en.getKey().getValue()).reduce(Double::sum).orElse(0.) / result.size() * 100;

        model.addAttribute("user", session.getUser());
        model.addAttribute("date", session.getDate());
        model.addAttribute("month", session.getDate().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()));
        model.addAttribute("finalScore", finalScore);
        model.addAttribute("result_map", result);


        return "presentation_of_session_results/presentation_of_session_results-page";
    }
}
