package JavaExam.controllers.pages;

import JavaExam.domain.*;
import JavaExam.service.ExamQuestionService;
import JavaExam.service.ExamSessionService;
import JavaExam.service.UserAnswerService;
import JavaExam.utils.Numbering;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/testing")
public class TestingController {
    private final ExamSessionService sessionService;
    private final ExamQuestionService questionService;
    private final UserAnswerService answerService;

    User user;
    ExamSession session;
    Integer questionNumber = 1; // номер текущего вопроса

    public TestingController(ExamSessionService sessionService, ExamQuestionService questionService, UserAnswerService answerService) {
        this.sessionService = sessionService;
        this.questionService = questionService;
        this.answerService = answerService;
    }

    @GetMapping
    public String displayPage(Model model, @AuthenticationPrincipal User user) {
        // загрузка нужной сессии
        if (this.user != user) {
            this.user = user;
            if (session != null) sessionService.save(session);
            session = sessionService.findByUser(user);
            //session.setAnswers(null);
        }

        // передача текущего вопроса на фронт
        if (session != null) {
            if (session.getAnswers() == null || session.getAnswers().size() == 0) sessionInitialization(session);

            if ((questionNumber - 1) < session.getAnswers().size()) {
                model.addAttribute("noSession", session == null);
                model.addAttribute("questionNumber", questionNumber);
                model.addAttribute("question", session.getAnswers().get(questionNumber - 1).getQuestion());
                model.addAttribute("numbering", new Numbering());
            } else {
                return "testing/testing-page-results";
            }


        } else model.addAttribute("noSession", true);

        return "testing/testing-page";
    }

    // присваивает сессии дату и список UserAnswer, в которых заполнено только поле ExamQuestion
    private void sessionInitialization(ExamSession session) {
        session.setDate(LocalDate.now());

        // получение листов с топиками и количествами вопросов
        ExamSessionSchema schema = session.getUser().getSessionSchema();
        List<ExamQuestionTopic> topics = schema.getUnits().stream()
                .map(unit -> unit.getTopics())
                .flatMap(unitTopics -> unitTopics.stream())
                .collect(Collectors.toList());
        List<Integer> quans = schema.getUnits().stream()
                .map(unit -> unit.getQuantityQuestions())
                .flatMap(unitQuans -> unitQuans.stream())
                .collect(Collectors.toList());

        // создание списка вопросов
        List<ExamQuestion> questions = new ArrayList<>(quans.stream().reduce(0, Integer::sum));
        for (int i = 0; i < topics.size(); i++) {
            questions.addAll(questionService.findRandomQuestionsByTopic(topics.get(i), quans.get(i)));
        }

        // сохранение вопросов в поле answers сессии (содержит не только ответ, но и вопрос)
        List<UserAnswer> answers = new ArrayList<>(questions.size());
        for (int i = 0; i < questions.size(); i++) {
            UserAnswer answer = new UserAnswer();
            answer.setSession(session);
            answer.setQuestion(questions.get(i));
            answers.add(answerService.save(answer));
        }

        session.setAnswers(answers);
        sessionService.update(session);
    }

    @GetMapping("accept_answer")
    public String acceptAnswer(@RequestParam("answer_number") byte answerNumber) {
        UserAnswer answer = session.getAnswers().get(questionNumber - 1);
        answer.setSelectedAnswerNumber(answerNumber);
        answerService.update(answer);

        questionNumber++;

        return "redirect:/testing";
    }
}