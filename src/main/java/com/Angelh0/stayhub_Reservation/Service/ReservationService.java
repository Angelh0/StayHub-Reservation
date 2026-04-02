package com.Angelh0.stayhub_Reservation.Service;

import com.Angelh0.stayhub_Reservation.dto.RequestReservationDTO;
import com.Angelh0.stayhub_Reservation.dto.ReservationDTO;
import com.Angelh0.stayhub_Reservation.dto.StatusCheckValue;
import com.Angelh0.stayhub_Reservation.entity.ReservationEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReservationService {

    ReservationDTO createReservation(UUID uuidRoom, UUID uuidUser, String userName, String userLastName, String userEmail);
    List<ReservationDTO> getMyReservation(UUID uuidUser);
    ReservationDTO cancelReservation(UUID uuidReservation, UUID uuidUser);
    List<ReservationDTO> getOwnerReservation(UUID uuidUser);
    boolean isFutureReservation(String uuid);
    boolean isRoomAvailable(String uuid, LocalDate checkIn, LocalDate checkOut);
    void confirmReservation();
    void changeReservationAsCompleted();

}