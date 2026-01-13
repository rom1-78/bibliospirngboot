package fr.ensitech.biblio.service.impl;

import fr.ensitech.biblio.dto.ReservationCreateDTO;
import fr.ensitech.biblio.dto.ReservationDTO;
import fr.ensitech.biblio.entity.Book;
import fr.ensitech.biblio.entity.Reservation;
import fr.ensitech.biblio.entity.User;
import fr.ensitech.biblio.enums.ReservationStatus;
import fr.ensitech.biblio.repository.IBookRepository;
import fr.ensitech.biblio.repository.IUserRepository;
import fr.ensitech.biblio.repository.ReservationRepository;
import fr.ensitech.biblio.service.ReservationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final IBookRepository bookRepository;
    private final IUserRepository userRepository;

    public ReservationServiceImpl(
            ReservationRepository reservationRepository,
            IBookRepository bookRepository,
            IUserRepository userRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ReservationDTO create(Long userId, ReservationCreateDTO dto) {
        User user = userRepository.findById(userId).orElseThrow();
        Book book = bookRepository.findById(dto.bookId()).orElseThrow();

        boolean alreadyReserved = reservationRepository.existsByBookAndStatusIn(
                book,
                List.of(ReservationStatus.CREATED, ReservationStatus.CONFIRMED)
        );

        if (alreadyReserved) {
            throw new IllegalStateException("Book already reserved");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setStartDate(dto.startDate());
        reservation.setEndDate(dto.endDate());

        reservationRepository.save(reservation);

        return new ReservationDTO(
                reservation.getId(),
                book.getId(),
                book.getTitle(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getStatus()
        );
    }

    @Override
    public void confirm(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow();
        reservation.setStatus(ReservationStatus.CONFIRMED);
    }

    @Override
    public void cancel(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow();
        reservation.setStatus(ReservationStatus.CANCELED);
    }

    @Override
    public void returnBook(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow();
        reservation.setStatus(ReservationStatus.RETURNED);
        reservation.setReturnDate(java.time.LocalDate.now());
    }

    @Override
    public List<ReservationDTO> getUserReservations(Long userId) {
        return reservationRepository.findByUserId(userId)
                .stream()
                .map(r -> new ReservationDTO(
                        r.getId(),
                        r.getBook().getId(),
                        r.getBook().getTitle(),
                        r.getStartDate(),
                        r.getEndDate(),
                        r.getStatus()
                ))
                .toList();
    }
}
