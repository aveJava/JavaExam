package JavaExam.domain;

import JavaExam.model.ExamSessionSchemaModel;
import JavaExam.model.ExamSessionSchemaUnitModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "session_schema")
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@SelectBeforeUpdate
public class ExamSessionSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany (mappedBy = "sessionSchema", fetch = FetchType.EAGER)
    private List<ExamSessionSchemaUnit> units;

    public ExamSessionSchemaModel toModel() {
        ExamSessionSchemaModel model = new ExamSessionSchemaModel();

        model.setId(id);
        model.setName(name);
        List<ExamSessionSchemaUnitModel> unitsModels = new ArrayList<>(10);
        if (units != null) {
            for (int i = 0; i < units.size(); i++) {
                unitsModels.add(units.get(i).toModel());
            }
        } else {

        }
        model.setUnits(unitsModels);

        return model;
    }

    public ExamSessionSchema() {
    }

    public ExamSessionSchema(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Map<ExamQuestionFieldOfKnowledge, Map<ExamQuestionTopic, Integer>> getSessionSchemaMap() {
        Map<ExamQuestionFieldOfKnowledge, Map<ExamQuestionTopic, Integer>> schemaMap = new HashMap<>(units.size());

        for (int i = 0; i < units.size(); i++) {
            ExamSessionSchemaUnit unit = units.get(i);
            ExamQuestionFieldOfKnowledge foKn = unit.getFoKn();
            Map<ExamQuestionTopic, Integer> unitMap = schemaMap.containsKey(foKn) ?
                    unit.stackUnitMaps(schemaMap.get(foKn)) : unit.getUnitMap();

            schemaMap.put(foKn, unitMap);
        }

        return schemaMap;
    }

}
