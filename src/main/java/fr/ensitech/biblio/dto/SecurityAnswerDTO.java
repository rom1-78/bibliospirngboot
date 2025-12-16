package fr.ensitech.biblio.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class SecurityAnswerDTO {

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email;

    @NotBlank(message = "La réponse de sécurité est obligatoire")
    private String securityAnswer;
}