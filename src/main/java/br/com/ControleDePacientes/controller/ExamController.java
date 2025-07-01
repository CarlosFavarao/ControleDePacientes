package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.ExamDto;
import br.com.ControleDePacientes.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam")
public class ExamController {

        @Autowired
        private ExamService examService;

        //Criação de exame
        @PostMapping("/schedule")
        public ResponseEntity<String> schedule(@RequestBody ExamDto dto) {
            try {
                examService.ScheduleExam(dto);
                return ResponseEntity.status(HttpStatus.CREATED).body("Exame agendado com sucesso.");
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }

        //Deletar exame
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }

    //Listar exames por paciente
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ExamDto>> listByPatients(@PathVariable Long patientId){
           List<ExamDto> exam = examService.listExamsByPatients(patientId);
           return ResponseEntity.ok(exam);
    }
}


