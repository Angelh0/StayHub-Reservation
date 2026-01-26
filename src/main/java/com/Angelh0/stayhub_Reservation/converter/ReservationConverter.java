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
        reservationDTO.setUuidUser(reservationEntity.getUuidUser());
        reservationDTO.setPrice(grpcResponse.getPrice());
        reservationDTO.setUuidReservation(reservationEntity.getUuidReservation());
        reservationDTO.setCheckIn(reservationEntity.getCheckIn());
        reservationDTO.setCheckOut(reservationEntity.getCheckOut());
        reservationDTO.setStatusReservation(reservationEntity.getStatusReservation());
        reservationDTO.setCreatedReservation(reservationEntity.getCreatedReservation());
        reservationDTO.setUuidOwner(grpcResponse.getUuidOwner());
        reservationDTO.setType(grpcResponse.getType());

        return reservationDTO;
    }

    public ReservationEntity requestToEntity(RequestReservationDTO requestReservationDTO) {
        ReservationEntity reservationEntity = new ReservationEntity();

        reservationEntity.setUuidReservation(requestReservationDTO.getUuidReservation());
        reservationEntity.setUuidUser(requestReservationDTO.getUuidUser());
        reservationEntity.setUuidRoom(requestReservationDTO.getUuidRoom());
        reservationEntity.setPrice(requestReservationDTO.getPrice());
        reservationEntity.setCheckIn(requestReservationDTO.getCheckIn());
        reservationEntity.setCheckOut(requestReservationDTO.getCheckOut());
        reservationEntity.setStatusReservation(requestReservationDTO.getStatusReservation());
        reservationEntity.setUuidOwner(requestReservationDTO.getUuidOwner());
        reservationEntity.setType(requestReservationDTO.getType());

        return reservationEntity;
    }
}