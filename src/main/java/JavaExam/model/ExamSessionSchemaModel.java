package JavaExam.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExamSessionSchemaModel {
    private String name;
    private List<ExamSessionSchemaUnitModel> units;
}
