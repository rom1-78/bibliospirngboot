package fr.ensitech.biblio.service;

import fr.ensitech.biblio.entity.PasswordHistory;
import fr.ensitech.biblio.entity.User;
import fr.ensitech.biblio.repository.IPasswordHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PasswordService {

    @Autowired
    private IPasswordHistoryRepository passwordHistoryRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Vérifie si le mot de passe a expiré (12 semaines = 84 jours)
     */
    public boolean isPasswordExpired(User user) {
        LocalDateTime expirationDate = user.getPasswordLastUpdated().plusWeeks(12);
        return LocalDateTime.now().isAfter(expirationDate);
    }

    /**
     * Vérifie si le nouveau mot de passe figure dans les 5 derniers
     */
    public boolean isPasswordInHistory(Long userId, String newPassword) {
        List<PasswordHistory> history = passwordHistoryRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId);

        for (PasswordHistory ph : history) {
            if (passwordEncoder.matches(newPassword, ph.getHashedPassword())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ajoute le mot de passe actuel à l'historique
     */
    @Transactional
    public void addPasswordToHistory(Long userId, String hashedPassword) {
        PasswordHistory history = new PasswordHistory();
        history.setUserId(userId);
        history.setHashedPassword(hashedPassword);
        history.setCreatedAt(LocalDateTime.now());

        passwordHistoryRepository.save(history);

        // Garder seulement les 5 derniers
        List<PasswordHistory> allHistory = passwordHistoryRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId);
        if (allHistory.size() > 5) {
            for (int i = 5; i < allHistory.size(); i++) {
                passwordHistoryRepository.delete(allHistory.get(i));
            }
        }
    }

    /**
     * Hash un mot de passe avec BCrypt
     */
    public String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    /**
     * Vérifie si un mot de passe correspond au hash
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }
}