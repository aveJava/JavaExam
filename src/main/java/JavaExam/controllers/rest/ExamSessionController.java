package JavaExam.controllers.rest;

import JavaExam.domain.ExamSession;
import JavaExam.domain.ExamSessionSchema;
import JavaExam.domain.User;
import JavaExam.model.ExamSessionSchemaModel;
import JavaExam.service.*;
import JavaExam.validation.ExamSessionSchemaModelValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/entities/exam_sessions")
public class ExamSessionController {
    private final ExamSessionSchemaModelValidator validator;
    private final ExamSessionService sessionService;
    private final ExamSessionSchemaUnitService unitService;
    private final ExamSessionSchemaService schemaService;
    private final ExamQuestionFieldOfKnowledgeService foKnService;
    private final ExamQuestionTopicService topicService;
    private final RoleService roleService;
    private final UserService userService;
    private final CredentialsCreator creator;

    public ExamSessionController(ExamSessionSchemaModelValidator validator, ExamSessionService sessionService, ExamSessionSchemaUnitService unitService,
                                 ExamSessionSchemaService schemaService, ExamQuestionFieldOfKnowledgeService foKnService,
                                 ExamQuestionTopicService topicService, RoleService roleService, UserService userService) {
        this.validator = validator;
        this.sessionService = sessionService;
        this.unitService = unitService;
        this.schemaService = schemaService;
        this.foKnService = foKnService;
        this.topicService = topicService;
        this.roleService = roleService;
        this.userService = userService;
        creator = new CredentialsCreator();
    }

    @PostMapping
    public String create(@ModelAttribute("SchemaModel") ExamSessionSchemaModel model, RedirectAttributes redirectAttr) {
        if (model == null) return "redirect:/admin/session_creation";

        // проверка валидности полученной модели, получение по этой модели объекта ExamSessionSchema, который будет присвоен создаваемой сессии
        ExamSessionSchema schemaForSession = null;
        final String validName = "since when creating a session it is not necessary to give a name to the schema, then this name is used so that the schema will pass validation";
        boolean needSaveSchema = model.getId() == null || ExamSessionSchemaModel.isSchemaChanged(schemaService, model);
        if (needSaveSchema) {
            model.setName(validName);
            boolean isValid = validateAndPrepareRedirectAttributesIfInvalid(model, redirectAttr);
            if (isValid) {
                model.setId(null);
                model.setName(null);
                ExamSessionSchema schema = model.toEntity(unitService, schemaService, foKnService, topicService);
                schemaForSession = schemaService.save(schema);
            } else {
                model.setName(null);
                return "redirect:/admin/session_creation";
            }
        } else schemaForSession = schemaService.get(model.getId()).get();

        // создание нового юзера
        Optional<User> usrOptional = Optional.empty();
        String username = null;
        String password = null;
        for (int i=0; i<100; i++) {
            username = creator.getUsername();
            password =creator.getPassword();
            User user = new User(username, password, schemaForSession, Set.of(roleService.findByName("ROLE_USER")));
            usrOptional = userService.save(user);
            if (usrOptional.isPresent()) break;
            else if (i == 99) {
                model.setName(null);
                redirectAttr.addFlashAttribute("msgs", List.of("Сохранения не произошло, т.к. возникли непредвиденные проблемы с сохранением новой учетной записи"));
                redirectAttr.addFlashAttribute("SchemaModel", model);

                return "redirect:/admin/session_creation";
            }
        }

        // создание сессии
        ExamSession session = new ExamSession();
        session.setUser(usrOptional.get());
        session.setDate(LocalDate.now());
        session.setCompleted(false);
        sessionService.save(session);

        redirectAttr.addFlashAttribute("showCredentials", true);
        redirectAttr.addFlashAttribute("username", username);
        redirectAttr.addFlashAttribute("password", password);

        return "redirect:/admin/session_creation";
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable("id") Long id) {
        sessionService.delete(sessionService.get(id));
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

class CredentialsCreator {
    private final int passLength = 20;

    protected String getUsername() {
        return "user_" + ((int) (Math.random() * 2147475800 + 7847));     // случайное число от 7847 до верхнего предела int
    }

    protected String getPassword() {
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < passLength; i++) {
            byte type = (byte) (Math.random() * 4);
            switch (type) {
                case 0:
                    password.append((byte) (Math.random() * 10));         // добавление цифры
                    break;
                case 1:
                    password.append((char) (Math.random() * 26 + 97));    // код маленькой буквы
                    break;
                case 2:
                    password.append((char) (Math.random() * 26 + 65));    // код большой буквы
                    break;
                case 3:
                    char[] symbol = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '=', '+', '/', '?', '<', '>', ':', ';', '~', '[', ']', '{', '}'};
                    password.append(symbol[(byte) (Math.random() * 25)]); // код спецсимвола
                    break;
            }
        }

        return password.toString();
    }
}
