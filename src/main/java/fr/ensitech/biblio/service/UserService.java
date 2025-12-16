package fr.ensitech.biblio.service;

import fr.ensitech.biblio.dto.*;
import fr.ensitech.biblio.entity.User;
import fr.ensitech.biblio.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    @Override
    public void createUser(User user) throws Exception {
        userRepository.save(user);
    }

    @Override
    public User getUserById(long id) throws Exception {
        Optional<User> optional = userRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public List<User> getUsersByBirthdate(Date dateInf, Date dateSup) throws Exception {
        return userRepository.findByBirthdateBetween(dateInf, dateSup);
    }

    @Override
    @Transactional
    public void registerUser(UserRegistrationDTO dto) throws Exception {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new Exception("Un utilisateur avec cet email existe déjà");
        }

        User user = new User();
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setBirthdate(dto.getBirthdate());
        user.setActive(false); // Par défaut inactif jusqu'à activation

        // Hash du mot de passe
        String hashedPassword = passwordService.hashPassword(dto.getPassword());
        user.setPassword(hashedPassword);
        user.setPasswordLastUpdated(LocalDateTime.now());

        // Question et réponse de sécurité
        user.setSecurityQuestion(dto.getSecurityQuestion());
        String hashedAnswer = passwordService.hashPassword(dto.getSecurityAnswer().toLowerCase().trim());
        user.setSecurityAnswer(hashedAnswer);

        user.setAwaiting2FA(false);

        // Sauvegarder l'utilisateur
        User savedUser = userRepository.save(user);

        // Ajouter le mot de passe initial à l'historique
        passwordService.addPasswordToHistory(savedUser.getId(), hashedPassword);
    }

    @Override
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO loginRequest) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());

        if (optionalUser.isEmpty()) {
            return new LoginResponseDTO(false, "Email ou mot de passe incorrect");
        }

        User user = optionalUser.get();

        // Vérifier si le compte est actif
        if (!user.isActive()) {
            return new LoginResponseDTO(false, "Compte non activé");
        }

        // Vérifier le mot de passe
        if (!passwordService.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
            return new LoginResponseDTO(false, "Email ou mot de passe incorrect");
        }

        // Vérifier si le mot de passe a expiré
        if (passwordService.isPasswordExpired(user)) {
            return new LoginResponseDTO(false, "Votre mot de passe a expiré. Veuillez le renouveler.");
        }

        // Marquer l'utilisateur en attente de 2FA
        user.setAwaiting2FA(true);
        userRepository.save(user);

        // Retourner la question de sécurité
        LoginResponseDTO response = new LoginResponseDTO();
        response.setSuccess(true);
        response.setMessage("Veuillez répondre à la question de sécurité");
        response.setSecurityQuestion(user.getSecurityQuestion().getQuestion());
        response.setRequires2FA(true);

        return response;
    }

    @Override
    @Transactional
    public LoginResponseDTO verify2FA(SecurityAnswerDTO answerDTO) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(answerDTO.getEmail());

        if (optionalUser.isEmpty()) {
            return new LoginResponseDTO(false, "Utilisateur non trouvé");
        }

        User user = optionalUser.get();

        // Vérifier si l'utilisateur est en attente de 2FA
        if (!user.isAwaiting2FA()) {
            return new LoginResponseDTO(false, "Veuillez d'abord vous connecter avec votre email et mot de passe");
        }

        // Vérifier la réponse de sécurité
        String normalizedAnswer = answerDTO.getSecurityAnswer().toLowerCase().trim();
        if (!passwordService.verifyPassword(normalizedAnswer, user.getSecurityAnswer())) {
            return new LoginResponseDTO(false, "Réponse de sécurité incorrecte");
        }

        // Authentification réussie
        user.setAwaiting2FA(false);
        userRepository.save(user);

        return new LoginResponseDTO(true, "Authentification réussie. Bienvenue " + user.getFirstname() + " !");
    }

    @Override
    @Transactional
    public void renewPassword(String email, PasswordRenewalDTO passwordRenewal) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new Exception("Utilisateur non trouvé");
        }

        User user = optionalUser.get();

        // Vérifier l'ancien mot de passe
        if (!passwordService.verifyPassword(passwordRenewal.getOldPassword(), user.getPassword())) {
            throw new Exception("L'ancien mot de passe est incorrect");
        }

        // Vérifier que le nouveau mot de passe n'est pas dans l'historique
        if (passwordService.isPasswordInHistory(user.getId(), passwordRenewal.getNewPassword())) {
            throw new Exception("Ce mot de passe a déjà été utilisé. Veuillez en choisir un nouveau.");
        }

        // Ajouter l'ancien mot de passe à l'historique
        passwordService.addPasswordToHistory(user.getId(), user.getPassword());

        // Hash et mise à jour du nouveau mot de passe
        String newHashedPassword = passwordService.hashPassword(passwordRenewal.getNewPassword());
        user.setPassword(newHashedPassword);
        user.setPasswordLastUpdated(LocalDateTime.now());

        userRepository.save(user);
    }
}