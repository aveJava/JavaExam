package JavaExam.controllers.pages;

import JavaExam.domain.ExamSessionSchema;
import JavaExam.model.ExamSessionSchemaModel;
import JavaExam.model.ExamSessionSchemaUnitModel;
import JavaExam.service.ExamQuestionFieldOfKnowledgeService;
import JavaExam.service.ExamQuestionTopicService;
import JavaExam.service.ExamSessionSchemaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/session_creation")
public class SessionCreationController {
    private final ExamQuestionFieldOfKnowledgeService foKnService;
    private final ExamQuestionTopicService topicService;
    private final ExamSessionSchemaService schemaService;

    private ExamSessionSchemaModel schemaModel;

    public SessionCreationController(ExamQuestionFieldOfKnowledgeService foKnService, ExamQuestionTopicService topicService, ExamSessionSchemaService schemaService) {
        this.foKnService = foKnService;
        this.topicService = topicService;
        this.schemaService = schemaService;
    }

    // отображает страницу
    @GetMapping
    public String displayPage(@RequestParam(name = "create_new", defaultValue = "false") boolean createNew, Model model) {
        if (schemaModel == null || createNew) createDefaultScheme();

        model.addAttribute("SchemaModel", schemaModel);
        model.addAttribute("fieldsOfKn", foKnService.getAll());
        model.addAttribute("topicService", topicService);

        return "session_creation/session_creation-page";
    }

    // создает новую схему с указанным числом юнитов
    @GetMapping("/new_schema")
    public String createNewSchema(@RequestParam("num_of_fields") Integer num) {
        if (num != null && num > 0 && num < 50) {
            schemaModel = new ExamSessionSchema().toModel();
            ArrayList<ExamSessionSchemaUnitModel> emptyUnits = new ArrayList<>(num);
            for (int i = 0; i < num; i++) {
                ExamSessionSchemaUnitModel unitModel = new ExamSessionSchemaUnitModel();
                emptyUnits.add(unitModel);
            }
            schemaModel.setUnits(emptyUnits);
        }

        return "redirect:/session_creation";
    }

    // обновляет схему при изменении foKn какого-либо юнита в html-форме
    @PostMapping("/update_schema")
    public String updateSchema(@ModelAttribute("SchemaModel") ExamSessionSchemaModel model) {
        if (model == null) return "redirect:/session_creation";

        List<ExamSessionSchemaUnitModel> units = schemaModel.getUnits();
        List<ExamSessionSchemaUnitModel> units1 = model.getUnits();
        for (int i = 0; i < units.size(); i++) {
            // если foKn юнита изменился, то сбросить все его топики и количества вопросов
            if (!units.get(i).getFoKn().equals(units1.get(i).getFoKn())) {
                units1.get(i).setTopics(new ArrayList<>());
                units1.get(i).setQuantityQuestions(new ArrayList<>());
            }
        }
        schemaModel = model;

        return "redirect:/session_creation";
    }

//    @GetMapping("/load_schema/{id}")
//    public String loadSchema(@PathVariable("id") Long id) {
//        Optional<ExamSessionSchema> optional = schemaService.get(id);
//        optional.ifPresent();
//
//    }

    // создание дефолтной схемы на 3 юнита
    private void createDefaultScheme() {
        ExamSessionSchemaUnitModel unitModel1 = new ExamSessionSchemaUnitModel();
        unitModel1.setFoKn("");
        ExamSessionSchemaUnitModel unitModel2 = new ExamSessionSchemaUnitModel();
        unitModel2.setFoKn("");
        ExamSessionSchemaUnitModel unitModel3 = new ExamSessionSchemaUnitModel();
        unitModel3.setFoKn("");
        schemaModel = new ExamSessionSchema().toModel();
        schemaModel.setUnits(List.of(unitModel1, unitModel2, unitModel3));
    }

}
