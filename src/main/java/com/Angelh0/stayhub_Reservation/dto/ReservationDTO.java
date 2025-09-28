package com.Angelh0.stayhub_Reservation.dto;

import com.Angelh0.stayhub_Reservation.Enum.StatusReservation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ReservationDTO {


    private UUID uuidReservation;

    private UUID uuidRoom;

    @JsonFormat(pattern="d/M/yyyy")
    private LocalDate checkIn;

    @JsonFormat(pattern="d/M/yyyy")
    private LocalDate checkOut;

    private StatusReservation statusReservation;

    @JsonFormat(pattern="d/M/yyyy")
    private LocalDate createdReservation;

    private Double price;

    //Añadir numero de personas, tipo de habitacion

}
