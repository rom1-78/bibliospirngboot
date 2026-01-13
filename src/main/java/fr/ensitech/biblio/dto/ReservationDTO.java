package fr.ensitech.biblio.dto;

import fr.ensitech.biblio.enums.ReservationStatus;

import java.time.LocalDate;

public record ReservationDTO(
        Long id,
        Long bookId,
        String bookTitle,
        LocalDate startDate,
        LocalDate endDate,
        ReservationStatus status
) {}
