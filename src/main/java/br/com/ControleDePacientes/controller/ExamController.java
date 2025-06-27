package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.ExamDto;
import br.com.ControleDePacientes.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exam")
public class ExamController {
    @Autowired
    private ExamService examService;

        @Autowired
        private ExamService exameService;

        @PostMapping("/schedule")
        public ResponseEntity<String> schedule(@RequestBody ExamDto dto) {
            try {
                exameService.ScheduleExam(dto);
                return ResponseEntity.status(HttpStatus.CREATED).body("Exame agendado com sucesso.");
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }
    }


