package com.Angelh0.stayhub_Reservation.Service.Impl;

import com.Angelh0.stayhub_Reservation.Enum.StatusReservation;
import com.Angelh0.stayhub_Reservation.Service.BlockService;
import com.Angelh0.stayhub_Reservation.Service.ReservationService;
import com.Angelh0.stayhub_Reservation.converter.ReservationConverter;
import com.Angelh0.stayhub_Reservation.dto.RequestReservationDTO;
import com.Angelh0.stayhub_Reservation.dto.ReservationDTO;
import com.Angelh0.stayhub_Reservation.dto.StatusCheckValue;
import com.Angelh0.stayhub_Reservation.entity.BlockEntity;
import com.Angelh0.stayhub_Reservation.entity.ReservationEntity;
import com.Angelh0.stayhub_Reservation.grpcClient.GrpcClientGetChecks;
import com.Angelh0.stayhub_Reservation.grpcClient.GrpcClientGetInfoRoom;
import com.Angelh0.stayhub_Reservation.repository.BlockRepository;
import com.Angelh0.stayhub_Reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Block;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
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

    @Autowired
    private final BlockService blockService;
    @Autowired
    private BlockRepository blockRepository;


    public ReservationServiceImpl(GrpcClientGetInfoRoom grpcClientGetInfoRoom, ReservationRepository reservationRepository, ReservationConverter reservationConverter, GrpcClientGetChecks grpcClientGetChecks, BlockService blockService) {
        this.grpcClientGetInfoRoom = grpcClientGetInfoRoom;
        this.reservationRepository = reservationRepository;
        this.reservationConverter = reservationConverter;
        this.grpcClientGetChecks = grpcClientGetChecks;
        this.blockService = blockService;
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
                    requestReservationDTO.getCheckIn().equals(requestReservationDTO.getCheckOut())) {
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
    public ReservationDTO getReservation(UUID uuid) {
        return null;
    }

    @Override
    public ReservationDTO deleteReservation(UUID uuid) {
        return null;
    }

    @Override
    public boolean isFutureReservation(String uuid) {

        /*
        True = existen reservas futuras
        false = no existen reservas futuras
        */

        LocalDate currentDay = LocalDate.now();

        List<ReservationEntity> reservationEntityList = reservationRepository.findByUuidRoom(UUID.fromString(uuid));

        for (ReservationEntity reservationEntity : reservationEntityList) {
            if (!reservationEntity.getCheckOut().isBefore(currentDay)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isRoomAvailable(String uuid, LocalDate checkIn, LocalDate checkOut) {

        List<BlockEntity> blockEntities = blockRepository.findBlockByUuidRoom(UUID.fromString(uuid));
        List<ReservationEntity> reservationEntityList = reservationRepository.findByUuidRoom(UUID.fromString(uuid));

        for (BlockEntity blockEntity : blockEntities) {
            if (checkIn.isBefore(blockEntity.getBlockEndDate()) && checkOut.isAfter(blockEntity.getBlockStartDate())) {
                return false;
            }
        }

        for (ReservationEntity reservationEntity : reservationEntityList) {
            if (checkIn.isBefore(reservationEntity.getCheckOut()) && checkOut.isAfter(reservationEntity.getCheckIn())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public StatusCheckValue isCheckStatus(String uuid, LocalDate startDate, LocalDate endDate) {

        StatusCheckValue status = new StatusCheckValue();
        StringBuilder stringBuilder = new StringBuilder();

        List<ReservationEntity> reservationEntityList = reservationRepository.findByUuidRoom(UUID.fromString(uuid));
        List<BlockEntity> blockEntities = blockRepository.findBlockByUuidRoom(UUID.fromString(uuid));

        for (ReservationEntity reservationEntity : reservationEntityList) {
            if (startDate.isBefore(reservationEntity.getCheckOut()) && endDate.isAfter(reservationEntity.getCheckIn()))  {
                stringBuilder.append("- Reserva del ")
                        .append(reservationEntity.getCheckIn())
                        .append(" al ")
                        .append(reservationEntity.getCheckOut())
                        .append("\n");
            }
        }


        for (BlockEntity blockEntity : blockEntities) {
            if (startDate.isBefore(blockEntity.getBlockEndDate()) && endDate.isAfter(blockEntity.getBlockStartDate())) {
                stringBuilder.append("- Bloqueo del ")
                        .append(blockEntity.getBlockStartDate())
                        .append(" al ")
                        .append(blockEntity.getBlockEndDate())
                        .append("\n");
            }
        }

        if (stringBuilder.length() > 0) {
            status.available = false;
            status.message = "No se puede completar la accion. Existen conflictos:\n " + stringBuilder;
            return status;
        }

        blockService.createBlock(UUID.fromString(uuid), startDate, endDate);

        status.available = true;
        status.message = "Nuevo bloqueo establecido:\n " +
                "- RoomUuid: " + uuid +
                "- Inicio de bloqueo: " + startDate +
                "- Finalizacion de bloqueo: " + endDate;
        return status;
    }
}