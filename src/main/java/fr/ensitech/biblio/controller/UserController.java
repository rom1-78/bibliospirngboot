package fr.ensitech.biblio.controller;

import fr.ensitech.biblio.dto.*;
import fr.ensitech.biblio.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * Inscription d'un nouvel utilisateur
     * POST /api/users/register
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        try {
            userService.registerUser(registrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new MessageResponse("Inscription réussie. Veuillez activer votre compte."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Erreur: " + e.getMessage()));
        }
    }

    /**
     * Étape 1 : Authentification avec email et mot de passe
     * POST /api/users/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            LoginResponseDTO response = userService.login(loginRequest);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponseDTO(false, "Erreur serveur: " + e.getMessage()));
        }
    }

    /**
     * Étape 2 : Vérification de la réponse à la question de sécurité (2FA)
     * POST /api/users/verify-2fa
     */
    @PostMapping("/verify-2fa")
    public ResponseEntity<?> verify2FA(@Valid @RequestBody SecurityAnswerDTO answerDTO) {
        try {
            LoginResponseDTO response = userService.verify2FA(answerDTO);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponseDTO(false, "Erreur serveur: " + e.getMessage()));
        }
    }

    /**
     * Renouvellement du mot de passe
     * PUT /api/users/{email}/password/renew
     */
    @PutMapping("/{email}/password/renew")
    public ResponseEntity<?> renewPassword(
            @PathVariable String email,
            @Valid @RequestBody PasswordRenewalDTO passwordRenewal) {
        try {
            userService.renewPassword(email, passwordRenewal);
            return ResponseEntity.ok(new MessageResponse("Mot de passe renouvelé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Erreur: " + e.getMessage()));
        }
    }

    // Classe interne pour les réponses simples
    @lombok.Getter @lombok.Setter @lombok.AllArgsConstructor
    static class MessageResponse {
        private String message;
    }
}