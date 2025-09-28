package com.Angelh0.stayhub_Reservation.entity;

import com.Angelh0.stayhub_Reservation.Enum.StatusReservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "RESERVATION_TABLE")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private UUID uuidReservation;

    private UUID uuidRoom;

    private LocalDate checkIn;

    private LocalDate checkOut;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusReservation statusReservation;

    private LocalDate createdReservation;

    private Double price;

    @PrePersist
    public void generateUUID() {
        if (uuidReservation == null) {
            uuidReservation = UUID.randomUUID();
        }
        if (createdReservation == null) {
            createdReservation = LocalDate.now();
        }
        if (statusReservation == null) {
            statusReservation = StatusReservation.Pending;
        }
    }
}