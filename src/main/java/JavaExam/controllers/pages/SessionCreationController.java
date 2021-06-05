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
import java.util.Optional;

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

        if (!model.containsAttribute("SchemaModel")) model.addAttribute("SchemaModel", schemaModel);
        model.addAttribute("fieldsOfKn", foKnService.getAll());
        model.addAttribute("topicService", topicService);
        model.addAttribute("schemesWithIdAndName", schemaService.getAllSchemesWithIdAndNameOnly());

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

        List<ExamSessionSchemaUnitModel> units1 = schemaModel.getUnits();
        List<ExamSessionSchemaUnitModel> units2 = model.getUnits();
        for (int i = 0; i < units1.size(); i++) {
            // если foKn юнита изменился, то сбросить все его топики и количества вопросов
            String foKn1 = units1.get(i).getFoKn();
            String foKn2 = units2.get(i).getFoKn();
            if (foKn1 != null && (foKn2 == null || !units1.get(i).getFoKn().equals(units2.get(i).getFoKn()))) {
                units2.get(i).setTopics(new ArrayList<>());
                units2.get(i).setQuantityQuestions(new ArrayList<>());
            }
        }
        schemaModel = model;

        return "redirect:/session_creation";
    }

    // загружает схему в html-форму из БД
    @GetMapping("/loading_schema")
    public String loadSchema(@RequestParam("schemaId") Long schemaId) {
        Optional<ExamSessionSchema> optional = schemaService.get(schemaId);
        optional.ifPresent(s -> schemaModel = s.toModel());

        return "redirect:/session_creation";
    }

    // добавить или удалить юнит
    @GetMapping("/add_or_remove_unit")
    public String addOrRemoveUnit(@ModelAttribute("SchemaModel") ExamSessionSchemaModel model, @RequestParam("action") String action) {
        ArrayList<ExamSessionSchemaUnitModel> units = new ArrayList<>(model.getUnits());
        if ("add".equals(action)) {
            ExamSessionSchemaUnitModel unitModel = new ExamSessionSchemaUnitModel();
            unitModel.setFoKn("");
            units.add(unitModel);
        }
        if ("remove".equals(action)) units.remove(units.size() - 1);
        schemaModel.setUnits(units);

        return "redirect:/session_creation";
    }

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
