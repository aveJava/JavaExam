package JavaExam.controllers.pages;

import JavaExam.domain.ExamQuestionFieldOfKnowledge;
import JavaExam.model.ExamSessionSchemaModel;
import JavaExam.model.ExamSessionSchemaUnitModel;
import JavaExam.service.ExamQuestionFieldOfKnowledgeService;
import JavaExam.service.ExamQuestionTopicService;
import JavaExam.service.ExamSessionSchemaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        // создание дефолтной схемы на 3 юнита
        ExamSessionSchemaUnitModel unitModel1 = new ExamSessionSchemaUnitModel();
        unitModel1.setFoKn("");
        ExamSessionSchemaUnitModel unitModel2 = new ExamSessionSchemaUnitModel();
        unitModel2.setFoKn("");
        ExamSessionSchemaUnitModel unitModel3 = new ExamSessionSchemaUnitModel();
        unitModel3.setFoKn("");
        schemaModel = new ExamSessionSchemaModel();
        schemaModel.setUnits(List.of(unitModel1, unitModel2, unitModel3));
    }

    // отображает страницу
    @GetMapping
    public String displayPage(Model model) {
        model.addAttribute("SchemaModel", schemaModel);
        model.addAttribute("fieldsOfKn", foKnService.getAll());
        model.addAttribute("topicService", topicService);

        return "session_creation/session_creation-page";
    }

    // создает новую схему с указанным числом юнитов
    @GetMapping("/new_schema")
    public String createNewSchema(@RequestParam("num_of_fields") Integer num) {
        if (num != null && num > 0 && num < 50) {
            schemaModel = new ExamSessionSchemaModel();
            ArrayList<ExamSessionSchemaUnitModel> emptyUnits = new ArrayList<>(num);
            for (int i = 0; i < num; i++) {
                ExamSessionSchemaUnitModel unitModel = new ExamSessionSchemaUnitModel();
                emptyUnits.add(unitModel);
            }
            schemaModel.setUnits(emptyUnits);
        }

        return "redirect:/session_creation";
    }

    // меняет FoKn (раздел) выбранного юнита схемы
    @GetMapping("/change_schema_foKn")
    public String changeSchemaFoKn(@RequestParam("unit_number") Integer num, @RequestParam("foKn_name") String name) {
        boolean isNumPresent = num != null && num >= 0 && num < schemaModel.getUnits().size();
        boolean isNamePresent = name != null && !name.isEmpty();
        ExamQuestionFieldOfKnowledge foKn = isNamePresent && isNumPresent ? foKnService.getByName(name) : null;
        if (foKn != null) {
            schemaModel.getUnits().get(num).setFoKn(name);
        }

        return "redirect:/session_creation";
    }

//    @GetMapping("/load_schema/{id}")
//    public String loadSchema(@PathVariable("id") Long id) {
//        Optional<ExamSessionSchema> optional = schemaService.get(id);
//        optional.ifPresent();
//
//    }


}
