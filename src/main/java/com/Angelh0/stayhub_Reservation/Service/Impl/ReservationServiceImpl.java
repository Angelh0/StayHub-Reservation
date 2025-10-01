package com.Angelh0.stayhub_Reservation.Service.Impl;

import com.Angelh0.stayhub_Reservation.Enum.StatusReservation;
import com.Angelh0.stayhub_Reservation.Service.ReservationService;
import com.Angelh0.stayhub_Reservation.converter.ReservationConverter;
import com.Angelh0.stayhub_Reservation.dto.RequestReservationDTO;
import com.Angelh0.stayhub_Reservation.dto.ReservationDTO;
import com.Angelh0.stayhub_Reservation.entity.ReservationEntity;
import com.Angelh0.stayhub_Reservation.grpcClient.GrpcClientGetChecks;
import com.Angelh0.stayhub_Reservation.grpcClient.GrpcClientGetInfoRoom;
import com.Angelh0.stayhub_Reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private final GrpcClientGetInfoRoom grpcClientGetInfoRoom;

    @Autowired
    private final ReservationRepository reservationRepository;

    @Autowired
    private final ReservationConverter reservationConverter;

    @Autowired
    private final GrpcClientGetChecks grpcClientGetChecks;


    public ReservationServiceImpl(GrpcClientGetInfoRoom grpcClientGetInfoRoom, ReservationRepository reservationRepository, ReservationConverter reservationConverter, GrpcClientGetChecks grpcClientGetChecks) {
        this.grpcClientGetInfoRoom = grpcClientGetInfoRoom;
        this.reservationRepository = reservationRepository;
        this.reservationConverter = reservationConverter;
        this.grpcClientGetChecks = grpcClientGetChecks;

    }

    @Override
    public ReservationDTO createReservation(UUID uuidRoom) {

        List<ReservationEntity> reservationEntityList = reservationRepository.findByUuidRoom(uuidRoom);

        ReservationDTO grpcResponse = grpcClientGetInfoRoom.getInfoRoom(uuidRoom);
        ReservationDTO checkResponse = grpcClientGetChecks.getCheckRoom();

        RequestReservationDTO requestReservationDTO = new RequestReservationDTO();
        requestReservationDTO.setUuidRoom(grpcResponse.getUuidRoom());
        requestReservationDTO.setPrice(grpcResponse.getPrice());
        requestReservationDTO.setCheckIn(checkResponse.getCheckIn());
        requestReservationDTO.setCheckOut(checkResponse.getCheckOut());
        requestReservationDTO.setStatusReservation(StatusReservation.Pending);


        for (ReservationEntity reservationEntity : reservationEntityList) {
            if (reservationEntity.getCheckIn().isBefore(requestReservationDTO.getCheckOut()) &&
                    reservationEntity.getCheckOut().isAfter(requestReservationDTO.getCheckIn()) &&
                    requestReservationDTO.getCheckIn().equals(requestReservationDTO.getCheckOut()))
               {
                return null;
            }
        }
        ReservationEntity reservationEntity = reservationConverter.requestToEntity(requestReservationDTO);
        reservationEntity = reservationRepository.save(reservationEntity);
        return reservationConverter.convertToDTO(reservationEntity);
    }

    @Override
    public ReservationEntity updateReservation(ReservationDTO reservationDTO) {
        return null;
    }

    @Override
    public ReservationDTO getReservation(UUID uuid) { return null; }

    @Override
    public ReservationDTO deleteReservation(UUID uuid) {
        return null;
    }

    @Override
    public boolean isRoomAvailable(String uuid, LocalDate checkIn, LocalDate checkOut) {

        List<ReservationEntity> reservationEntityList = reservationRepository.findByUuidRoom(UUID.fromString(uuid));

        for (ReservationEntity reservationEntity : reservationEntityList) {
            if (reservationEntity.getCheckIn().isBefore(checkOut) &&
                    reservationEntity.getCheckOut().isAfter(checkIn)) {
                return false;
            }
        } return true;
    }
}