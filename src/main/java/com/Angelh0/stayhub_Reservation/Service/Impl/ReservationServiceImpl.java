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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
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
    public ReservationDTO createReservation(UUID uuidRoom, UUID uuidUser, String userName, String lastName, String userEmail) {
        List<ReservationEntity> reservationEntityList = reservationRepository.findByUuidRoom(uuidRoom);

        ReservationDTO grpcResponse = grpcClientGetInfoRoom.getInfoRoom(uuidRoom);
        ReservationDTO checkResponse = grpcClientGetChecks.getCheckRoom();

        RequestReservationDTO requestReservationDTO = new RequestReservationDTO();
        requestReservationDTO.setUuidAccommodation(grpcResponse.getUuidAccommodation());
        requestReservationDTO.setNameAccommodation(grpcResponse.getNameAccommodation());
        requestReservationDTO.setUserName(userName);
        requestReservationDTO.setUserLastName(lastName);
        requestReservationDTO.setUserEmail(userEmail);
        requestReservationDTO.setStatusReservation(StatusReservation.Pending);
        requestReservationDTO.setUuidUser(uuidUser);
        requestReservationDTO.setUuidOwner(grpcResponse.getUuidOwner());
        requestReservationDTO.setCheckIn(checkResponse.getCheckIn());
        requestReservationDTO.setCheckOut(checkResponse.getCheckOut());
        requestReservationDTO.setUuidRoom(grpcResponse.getUuidRoom());
        requestReservationDTO.setPrice(grpcResponse.getPrice());
        requestReservationDTO.setType(grpcResponse.getType());

        for (ReservationEntity res : reservationEntityList) {
            if (res.getStatusReservation() != StatusReservation.cancelled) {
                boolean overlap = requestReservationDTO.getCheckIn().isBefore(res.getCheckOut()) &&
                        res.getCheckIn().isBefore(requestReservationDTO.getCheckOut());
                if (overlap) {
                    return null;
                }
            }
        }

        ReservationEntity reservationEntity = reservationConverter.requestToEntity(requestReservationDTO);
        reservationEntity = reservationRepository.save(reservationEntity);
        return reservationConverter.convertToDTO(reservationEntity);
    }

    @Override
    public List<ReservationDTO> getMyReservation(UUID uuidUser) {

        List<ReservationEntity> reservation = reservationRepository.findByUuidUser(uuidUser);

        if (reservation.isEmpty()) {
            return null;
        }

        List<ReservationDTO> reservationDTOS = new ArrayList<>();

        for (ReservationEntity reservationEntity : reservation) {
            if (reservationEntity.getUuidUser().equals(uuidUser)) {
                reservationDTOS.add(reservationConverter.convertToDTO(reservationEntity));
            }
        }
        return reservationDTOS;
    }

    @Override
    public ReservationDTO cancelReservation(UUID uuidReservation, UUID uuidUser) {

        Optional<ReservationEntity> reservationOpt = reservationRepository.findByUuidReservationAndUuidUser(uuidReservation,uuidUser);

        if (reservationOpt.isPresent()) {

            ReservationEntity reservation = reservationOpt.get();
            reservation.setStatusReservation(StatusReservation.cancelled);

            reservationRepository.save(reservation);

            return reservationConverter.convertToDTO(reservation);
        }

        return null;
    }

    @Override
    public List<ReservationDTO> getOwnerReservation(UUID uuidUser) {

        List<ReservationEntity> reservationEntityList = reservationRepository.findByUuidOwner(uuidUser);

        List<ReservationDTO> reservationDTOS = new ArrayList<>();

        for (ReservationEntity reservationEntity : reservationEntityList) {
            if (uuidUser.equals(reservationEntity.getUuidOwner())) {
                reservationDTOS.add(reservationConverter.convertToDTO(reservationEntity));
            }
        }

        return reservationDTOS;
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

        for (BlockEntity block : blockEntities) {
            if (checkIn.isBefore(block.getBlockEndDate()) && checkOut.isAfter(block.getBlockStartDate())) {
                return false;
            }
        }

        for (ReservationEntity reservation : reservationEntityList) {
            if (reservation.getStatusReservation() != StatusReservation.cancelled) {

                boolean conflict = checkIn.isBefore(reservation.getCheckOut()) && checkOut.isAfter(reservation.getCheckIn());

                if (conflict) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void confirmReservation() {
        LocalDateTime fiveMinutes = LocalDateTime.now().minusMinutes(5);

        List<ReservationEntity> pending = reservationRepository.findByStatusReservationAndCreatedReservationBefore(StatusReservation.Pending, fiveMinutes);

        if (!pending.isEmpty()) {
            for (ReservationEntity reservationEntity : pending) {
                reservationEntity.setStatusReservation(StatusReservation.Confirmed);
                System.out.println("Reserva " + reservationEntity.getUuidReservation() + " auto conformación");
            }
            reservationRepository.saveAll(pending);
        }
    }

    @Override
    @Scheduled(cron = "0 0 11 * * ?")
    @Transactional
    public void changeReservationAsCompleted() {
        LocalDate currentDay = LocalDate.now();

        List<ReservationEntity> finished = reservationRepository.findByStatusReservationAndCheckOutLessThanEqual(StatusReservation.Confirmed, currentDay);

        for (ReservationEntity reservationEntity : finished) {
            reservationEntity.setStatusReservation(StatusReservation.Completed);
        }

        reservationRepository.saveAll(finished);
    }
}