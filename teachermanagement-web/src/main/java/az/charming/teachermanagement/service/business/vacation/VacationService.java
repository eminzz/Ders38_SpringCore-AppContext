package az.charming.teachermanagement.service.business.vacation;

import az.charming.teachermanagement.dto.StudentDto;
import az.charming.teachermanagement.service.fuctional.StudentService1;
import org.springframework.stereotype.Service;

@Service
public class VacationService {

    private StudentService1 studentService1;

    public void submitVacation(VacationSubmitDto vacationSubmitDto){
        studentService1.save(new StudentDto().setName(vacationSubmitDto.getSubmitter()));
    }
}
