package fr.ensitech.biblio.dto;

import lombok.*;

@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class LoginResponseDTO {

    private boolean success;
    private String message;
    private String securityQuestion;
    private boolean requires2FA;

    public LoginResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.requires2FA = false;
    }
}