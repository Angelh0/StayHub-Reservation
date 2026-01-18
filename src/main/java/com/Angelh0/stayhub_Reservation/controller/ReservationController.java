package com.Angelh0.stayhub_Reservation.controller;

import com.Angelh0.stayhub_Reservation.Service.ReservationService;
import com.Angelh0.stayhub_Reservation.dto.RequestReservationDTO;
import com.Angelh0.stayhub_Reservation.dto.ReservationDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/createReservation/{uuidRoom}")
    public ReservationDTO createReservation(@PathVariable UUID uuidRoom, Authentication authentication) {
        UUID uuidUser = UUID.fromString(authentication.getPrincipal().toString());
        return reservationService.createReservation(uuidRoom, uuidUser);
    }
}