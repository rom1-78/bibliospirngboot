package fr.ensitech.biblio.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "users", catalog = "biblio_database")
@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "firstname", nullable = false, length = 48)
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 48)
    private String lastname;

    @Column(name = "email", nullable = false, length = 48, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "role", nullable = false, length = 1)
    private String role;

    @Column(name = "birthdate", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date birthdate;

    @Column(name = "active", nullable = false)
    private boolean active;

    // Nouveaux champs pour la sécurité
    @Enumerated(EnumType.STRING)
    @Column(name = "security_question", nullable = false)
    private SecurityQuestion securityQuestion;

    @Column(name = "security_answer", nullable = false, length = 255)
    private String securityAnswer;

    @Column(name = "password_last_updated", nullable = false)
    private LocalDateTime passwordLastUpdated;

    @Column(name = "awaiting_2fa", nullable = false)
    private boolean awaiting2FA = false;
}