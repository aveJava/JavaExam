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
import java.util.List;

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
}
