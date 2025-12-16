package fr.ensitech.biblio.service;

import fr.ensitech.biblio.dto.*;
import fr.ensitech.biblio.entity.User;
import java.util.Date;
import java.util.List;

public interface IUserService {

    // Méthodes existantes
    void createUser(User user) throws Exception;
    User getUserById(long id) throws Exception;
    List<User> getUsersByBirthdate(Date dateInf, Date dateSup) throws Exception;

    // Nouvelles méthodes pour le devoir
    void registerUser(UserRegistrationDTO registrationDTO) throws Exception;
    LoginResponseDTO login(LoginRequestDTO loginRequest) throws Exception;
    LoginResponseDTO verify2FA(SecurityAnswerDTO answerDTO) throws Exception;
    void renewPassword(String email, PasswordRenewalDTO passwordRenewal) throws Exception;
}