package JavaExam.controllers.rest;

import JavaExam.domain.ExamSessionSchema;
import JavaExam.model.ExamSessionSchemaModel;
import JavaExam.service.ExamQuestionFieldOfKnowledgeService;
import JavaExam.service.ExamQuestionTopicService;
import JavaExam.service.ExamSessionSchemaService;
import JavaExam.service.ExamSessionSchemaUnitService;
import JavaExam.validation.ExamSessionSchemaModelValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/session_schemes")
public class ExamSessionSchemaController {
    ExamSessionSchemaService schemaService;
    ExamSessionSchemaUnitService unitService;
    ExamQuestionFieldOfKnowledgeService foKnService;
    ExamQuestionTopicService topicService;
    ExamSessionSchemaModelValidator validator;

    public ExamSessionSchemaController(ExamSessionSchemaService schemaService, ExamSessionSchemaUnitService unitService,
                                       ExamQuestionFieldOfKnowledgeService foKnService, ExamQuestionTopicService topicService,
                                       ExamSessionSchemaModelValidator validator) {
        this.schemaService = schemaService;
        this.unitService = unitService;
        this.foKnService = foKnService;
        this.topicService = topicService;
        this.validator = validator;
    }

    @PostMapping
    public String create(@ModelAttribute("SchemaModel") ExamSessionSchemaModel model, RedirectAttributes redirectAttr) {
        boolean isValid = validateAndPrepareRedirectAttributesIfInvalid(model, redirectAttr);
        if (isValid) {
            ExamSessionSchema schema = model.toEntity(unitService, schemaService, foKnService, topicService);
            schemaService.save(schema);
            redirectAttr.addFlashAttribute("msgs", List.of("Вопрос сохранен!"));
        }

        return "redirect:/session_creation?create_new=true";
    }

    // валидирует заполненную форму создания или редактирования вопроса
    // если данные не валидны, подготавливает RedirectAttributes для перенаправления на повторное заполнение формы
    // возвращает true, если форма была заполнена правильно, false - если неправильно.
    public boolean validateAndPrepareRedirectAttributesIfInvalid(ExamSessionSchemaModel model, RedirectAttributes redirectAttr) {
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
            redirectAttr.addFlashAttribute("SchemaModel", model);
        }

        return !result.hasErrors();
    }

}
