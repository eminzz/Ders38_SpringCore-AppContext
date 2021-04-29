package az.charming.teachermanagement.service.fuctional;

import az.charming.teachermanagement.dto.StudentDto;
import az.charming.teachermanagement.entity.StudentEntity;

import java.math.BigDecimal;
import java.util.List;

public interface StudentServiceInter {

    public void getStudents();

    public void doSomething(StudentDto studentDto);

    public StudentDto findById(Integer id);

    public List<StudentDto> findAll(String name, String surname, Integer age,
                                    BigDecimal scholarship);

    public List<StudentEntity> findByAgeAndName(String name, Integer age);

    public StudentDto deleteById(Integer id);

    public void save(StudentDto studentDto);

    public void update(StudentDto studentDto);
}
