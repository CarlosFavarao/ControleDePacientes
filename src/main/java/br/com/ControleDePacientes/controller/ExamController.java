package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.ExamDto;
import br.com.ControleDePacientes.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    @PostMapping("/schedule")
    public ResponseEntity<ExamDto> scheduleExam(@RequestBody @Validated ExamDto examDto) {
        ExamDto scheduledExam = examService.scheduleExam(examDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduledExam);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamDto> updateExam(@PathVariable Long id, @RequestBody @Validated ExamDto examDto) {
        ExamDto updatedExam = examService.updateExam(id, examDto);
        return ResponseEntity.ok(updatedExam);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamDto> getExamById(@PathVariable Long id) {
        ExamDto examDto = examService.findByIdDto(id);
        return ResponseEntity.ok(examDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ExamDto>> listByPatients(@PathVariable Long patientId){
        List<ExamDto> exams = examService.listExamsByPatients(patientId);
        return ResponseEntity.ok(exams);
    }
}
