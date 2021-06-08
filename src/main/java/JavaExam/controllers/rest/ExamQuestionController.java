package JavaExam.controllers.rest;

import JavaExam.domain.ExamQuestion;
import JavaExam.model.ExamQuestionModel;
import JavaExam.service.ExamQuestionService;
import JavaExam.service.ExamQuestionTopicService;
import JavaExam.validation.ExamQuestionModelValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/entities/exam_questions")
public class ExamQuestionController {
    private final ExamQuestionTopicService topicService;
    private final ExamQuestionService questionService;
    private final ExamQuestionModelValidator validator;

    public ExamQuestionController(ExamQuestionTopicService topicService, ExamQuestionService questionService, ExamQuestionModelValidator validator) {
        this.topicService = topicService;
        this.questionService = questionService;
        this.validator = validator;
    }

    @PostMapping
    public String create(@ModelAttribute("model") ExamQuestionModel model, RedirectAttributes redirectAttr) {
        boolean isValid = validateAndPrepareRedirectAttributesIfInvalid(model, redirectAttr);
        if (isValid) {
            ExamQuestion question = model.toEntity(topicService);
            questionService.save(question);
            redirectAttr.addFlashAttribute("msgs", List.of("Вопрос сохранен!"));
        }

        return "redirect:/editing_tests";
    }

    @GetMapping("{id}/edit")
    public String updateForm(@PathVariable("id") Long id, RedirectAttributes redirectAttr) {
        redirectAttr.addFlashAttribute("model", questionService.get(id).toModel());
        redirectAttr.addFlashAttribute("editing", true);
        redirectAttr.addFlashAttribute("editId", id);
        return "redirect:/editing_tests";
    }

    @PatchMapping("{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute("model") ExamQuestionModel model, RedirectAttributes redirectAttr) {
        boolean isValid = validateAndPrepareRedirectAttributesIfInvalid(model, redirectAttr);
        if (isValid) {
            ExamQuestion question = model.toEntity(topicService);
            questionService.update(question);
            return "redirect:/editing_tests/switch?tab=VIEW_QUESTIONS";
        }

        return "redirect:/editing_tests";
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") Long id) {
        questionService.delete(questionService.get(id));
        return "redirect:/editing_tests";
    }

    // валидирует заполненную форму создания или редактирования вопроса
    // если данные не валидны, подготавливает RedirectAttributes для перенаправления на повторное заполнение формы
    // возвращает true, если форма была заполнена правильно, false - если неправильно.
    public boolean validateAndPrepareRedirectAttributesIfInvalid(ExamQuestionModel model, RedirectAttributes redirectAttr) {
        DataBinder dataBinder = new DataBinder(model);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        BindingResult result = dataBinder.getBindingResult();

        // создаем список сообщений об ошибках, отправляемый пользователю
        List<String> errorMessages = new ArrayList<>();
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
        }

        if (result.hasErrors()) {
            redirectAttr.addFlashAttribute("errors", errorMessages);
            redirectAttr.addFlashAttribute("model", model);
        }

        return !result.hasErrors();
    }
}
