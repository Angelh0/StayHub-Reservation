package com.Angelh0.stayhub_Reservation.repository;

import com.Angelh0.stayhub_Reservation.Enum.StatusReservation;
import com.Angelh0.stayhub_Reservation.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByUuidRoom(UUID uuidRoom);
    List<ReservationEntity> findByUuidUser(UUID uuidUser);

    List<ReservationEntity> findByUuidOwner(UUID uuidOwner);

    Optional<ReservationEntity> findByUuidReservationAndUuidUser(UUID uuidReservation, UUID uuidUser);

    List<ReservationEntity> findByStatusReservationAndCreatedReservationBefore(StatusReservation status, LocalDateTime time);

    List<ReservationEntity> findByStatusReservationAndCheckOutLessThanEqual(StatusReservation statusReservation, LocalDate checkOut);
}