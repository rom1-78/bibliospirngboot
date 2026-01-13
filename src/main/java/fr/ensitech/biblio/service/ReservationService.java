package fr.ensitech.biblio.service;

import fr.ensitech.biblio.dto.ReservationCreateDTO;
import fr.ensitech.biblio.dto.ReservationDTO;

import java.util.List;

public interface ReservationService {

    ReservationDTO create(Long userId, ReservationCreateDTO dto);

    void confirm(Long reservationId);

    void cancel(Long reservationId);

    void returnBook(Long reservationId);

    List<ReservationDTO> getUserReservations(Long userId);
}
