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

    @OneToMany (mappedBy = "sessionSchema")
    private List<ExamSessionSchemaUnit> units;

    public ExamSessionSchemaModel toModel() {
        ExamSessionSchemaModel model = new ExamSessionSchemaModel();

        model.setName(name);
        List<ExamSessionSchemaUnitModel> unitsModels = new ArrayList<>(units.size());
        for (int i = 0; i < units.size(); i++) {
            unitsModels.add(units.get(i).toModel());
        }
        model.setUnits(unitsModels);

        return model;
    }

}
