package com.Angelh0.stayhub_Reservation.dto;

import com.Angelh0.stayhub_Reservation.Enum.StatusReservation;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.roomServiceGrpc.grpc.ReservationType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class RequestReservationDTO {

    private UUID uuidReservation;

    private UUID uuidRoom;

    private UUID uuidUser;

    private UUID uuidOwner;

    private UUID uuidAccommodation;

    private String nameAccommodation;

    private String userName;
    private String userLastName;
    private String userEmail;

    @JsonFormat(pattern="d/M/yyyy")
    private LocalDate checkIn;

    @JsonFormat(pattern="d/M/yyyy")
    private LocalDate checkOut;

    @JsonFormat(pattern="d/M/yyyy")
    private LocalDate createdReservation;

    private StatusReservation statusReservation;

    private Double price;

    private ReservationType type;
}