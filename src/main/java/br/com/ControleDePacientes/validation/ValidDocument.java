package br.com.ControleDePacientes.validation;

 import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DocumentValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDocument {
    String message() default "Documento inválido. Insira um CPF ou RG válido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}