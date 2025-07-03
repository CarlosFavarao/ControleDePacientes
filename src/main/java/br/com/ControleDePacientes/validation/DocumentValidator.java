package br.com.ControleDePacientes.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DocumentValidator implements ConstraintValidator<ValidDocument, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) return false;

        String documento = value.replaceAll("[^\\d]", "");

        return isCPF(documento) || isRG(documento);
    }

    private boolean isRG(String rg) {
        // RG pode ter de 7 a 9 dígitos numéricos (sem validação de dígito verificador)
        return rg.matches("\\d{7,9}");
    }

    private boolean isCPF(String cpf) {
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * (10 - i);
            }
            int dig1 = 11 - (soma % 11);
            if (dig1 >= 10) dig1 = 0;

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * (11 - i);
            }
            int dig2 = 11 - (soma % 11);
            if (dig2 >= 10) dig2 = 0;

            return cpf.charAt(9) - '0' == dig1 && cpf.charAt(10) - '0' == dig2;
        } catch (Exception e) {
            return false;
        }
    }
}