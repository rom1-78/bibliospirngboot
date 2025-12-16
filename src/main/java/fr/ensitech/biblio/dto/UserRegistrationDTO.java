package fr.ensitech.biblio.dto;

import fr.ensitech.biblio.entity.SecurityQuestion;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Date;

@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class UserRegistrationDTO {

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 48, message = "Le prénom ne doit pas dépasser 48 caractères")
    private String firstname;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 48, message = "Le nom ne doit pas dépasser 48 caractères")
    private String lastname;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    @Size(max = 48, message = "L'email ne doit pas dépasser 48 caractères")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;

    @NotBlank(message = "Le rôle est obligatoire")
    @Pattern(regexp = "[UAB]", message = "Le rôle doit être U (User), A (Admin) ou B (Banned)")
    private String role;

    private Date birthdate;

    @NotNull(message = "La question de sécurité est obligatoire")
    private SecurityQuestion securityQuestion;

    @NotBlank(message = "La réponse de sécurité est obligatoire")
    @Size(max = 32, message = "La réponse ne doit pas dépasser 32 caractères")
    private String securityAnswer;
}