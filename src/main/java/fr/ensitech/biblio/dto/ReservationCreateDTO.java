package fr.ensitech.biblio.dto;

import java.time.LocalDate;

public record ReservationCreateDTO(
        Long bookId,
        LocalDate startDate,
        LocalDate endDate
) {}
