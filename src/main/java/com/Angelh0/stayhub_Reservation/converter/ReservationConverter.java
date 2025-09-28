package com.Angelh0.stayhub_Reservation.converter;

import com.Angelh0.stayhub_Reservation.dto.RequestReservationDTO;
import com.Angelh0.stayhub_Reservation.dto.ReservationDTO;
import com.Angelh0.stayhub_Reservation.entity.ReservationEntity;
import com.Angelh0.stayhub_Reservation.grpcClient.GrpcClientGetInfoRoom;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ReservationConverter {

    private final GrpcClientGetInfoRoom grpcClientGetInfoRoom;


    public ReservationConverter(GrpcClientGetInfoRoom grpcClientGetInfoRoom) {
        this.grpcClientGetInfoRoom = grpcClientGetInfoRoom;
    }


    public ReservationDTO convertToDTO(ReservationEntity reservationEntity) {
        ReservationDTO reservationDTO = new ReservationDTO();

        ReservationDTO grpcResponse = grpcClientGetInfoRoom.getInfoRoom(
                reservationEntity.getUuidRoom()
        );

        reservationDTO.setUuidRoom(grpcResponse.getUuidRoom());
        reservationDTO.setPrice(grpcResponse.getPrice());
        reservationDTO.setUuidReservation(reservationEntity.getUuidReservation());
        reservationDTO.setCheckIn(reservationEntity.getCheckIn());
        reservationDTO.setCheckOut(reservationEntity.getCheckOut());
        reservationDTO.setStatusReservation(reservationEntity.getStatusReservation());
        reservationDTO.setCreatedReservation(reservationEntity.getCreatedReservation());

        return reservationDTO;
    }

    public ReservationEntity requestToEntity(RequestReservationDTO requestReservationDTO) {
        ReservationEntity reservationEntity = new ReservationEntity();

        reservationEntity.setUuidReservation(requestReservationDTO.getUuidReservation());
        reservationEntity.setUuidRoom(requestReservationDTO.getUuidRoom());
        reservationEntity.setPrice(requestReservationDTO.getPrice());
        reservationEntity.setCheckIn(requestReservationDTO.getCheckIn());
        reservationEntity.setCheckOut(requestReservationDTO.getCheckOut());
        reservationEntity.setStatusReservation(requestReservationDTO.getStatusReservation());

        return reservationEntity;
    }
}