package com.Angelh0.stayhub_Reservation.entity;

import com.Angelh0.stayhub_Reservation.Enum.StatusReservation;
import com.roomServiceGrpc.grpc.ReservationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    private UUID uuidAccommodation;

    private UUID uuidRoom;

    private UUID uuidUser;

    private UUID uuidOwner;

    private String nameAccommodation;

    private String userName;

    private String userLastName;

    private String userEmail;

    private LocalDate checkIn;

    private LocalDate checkOut;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusReservation statusReservation;

    private LocalDateTime createdReservation;

    private Double price;

    private ReservationType type;

    @PrePersist
    public void generateUUID() {
        if (uuidReservation == null) {
            uuidReservation = UUID.randomUUID();
        }
        if (createdReservation == null) {
            createdReservation = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        }
        if (statusReservation == null) {
            statusReservation = StatusReservation.Pending;
        }
    }
}