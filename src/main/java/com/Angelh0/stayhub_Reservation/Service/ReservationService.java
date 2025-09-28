package com.Angelh0.stayhub_Reservation.Service;

import com.Angelh0.stayhub_Reservation.dto.RequestReservationDTO;
import com.Angelh0.stayhub_Reservation.dto.ReservationDTO;
import com.Angelh0.stayhub_Reservation.entity.ReservationEntity;

import java.time.LocalDate;
import java.util.UUID;

public interface ReservationService {

    ReservationDTO createReservation(UUID uuidRoom);
    ReservationEntity updateReservation(ReservationDTO reservationDTO);
    ReservationDTO getReservation(UUID uuid);
    ReservationDTO deleteReservation(UUID uuid);
    boolean isRoomAvailable(String uuid, LocalDate checkIn, LocalDate checkOut);
}