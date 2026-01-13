package fr.ensitech.biblio.controller;

import fr.ensitech.biblio.dto.ReservationCreateDTO;
import fr.ensitech.biblio.dto.ReservationDTO;
import fr.ensitech.biblio.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/users/{userId}")
    public ReservationDTO create(
            @PathVariable Long userId,
            @RequestBody ReservationCreateDTO dto
    ) {
        return reservationService.create(userId, dto);
    }

    @PostMapping("/{id}/confirm")
    public void confirm(@PathVariable Long id) {
        reservationService.confirm(id);
    }

    @PostMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id) {
        reservationService.cancel(id);
    }

    @PostMapping("/{id}/return")
    public void returnBook(@PathVariable Long id) {
        reservationService.returnBook(id);
    }

    @GetMapping("/users/{userId}")
    public List<ReservationDTO> userReservations(@PathVariable Long userId) {
        return reservationService.getUserReservations(userId);
    }
}
