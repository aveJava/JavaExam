package JavaExam.controllers.pages;

import JavaExam.domain.ExamSession;
import JavaExam.service.ExamSessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/test_results")
public class TestResultsController {
    private final ExamSessionService sessionService;

    public TestResultsController(ExamSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public String displayPage(Model model) {
        List<ExamSession> completedSessions = sessionService.findAllCompleted();
        Map<String, String> resultsMap = new HashMap<>(completedSessions.size());
        completedSessions.forEach((s) -> {
            String userName = s.getUser().getUsername();
            String href = "/results/" + s.getId();
            resultsMap.put(userName, href);
        });
        model.addAttribute("resultsMap", resultsMap);

        return "test_results_list/test_results_list-page";
    }
}
