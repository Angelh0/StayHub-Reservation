package com.Angelh0.stayhub_Reservation.Service.Impl;

import com.Angelh0.stayhub_Reservation.Service.BusinessService;
import com.Angelh0.stayhub_Reservation.dto.BlockDTO;
import com.Angelh0.stayhub_Reservation.dto.StatusCheckValue;
import com.Angelh0.stayhub_Reservation.entity.BlockEntity;
import com.Angelh0.stayhub_Reservation.entity.ReservationEntity;
import com.Angelh0.stayhub_Reservation.repository.BlockRepository;
import com.Angelh0.stayhub_Reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private final ReservationRepository reservationRepository;


    @Autowired
    private BlockRepository blockRepository;


    public BusinessServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public StatusCheckValue isCheckStatus(String uuid, String uuidOwner, LocalDate startDate, LocalDate endDate, String blockType, String reason) {

        StatusCheckValue status = new StatusCheckValue();
        StringBuilder stringBuilder = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        UUID roomUuid = UUID.fromString(uuid);
        List<ReservationEntity> reservationEntityList = reservationRepository.findByUuidRoom(roomUuid);
        List<BlockEntity> blockEntities = blockRepository.findBlockByUuidRoom(roomUuid);

        for (ReservationEntity reservationEntity : reservationEntityList) {
            if (startDate.isBefore(reservationEntity.getCheckOut()) && endDate.isAfter(reservationEntity.getCheckIn())) {
                stringBuilder.append("- Reserva del ")
                        .append(reservationEntity.getCheckIn().format(formatter))
                        .append(" al ")
                        .append(reservationEntity.getCheckOut().format(formatter))
                        .append("\n");
            }
        }

        for (BlockEntity blockEntity : blockEntities) {
            if (startDate.isBefore(blockEntity.getBlockEndDate()) && endDate.isAfter(blockEntity.getBlockStartDate())) {
                stringBuilder.append("- Bloqueo del ")
                        .append(blockEntity.getBlockStartDate().format(formatter))
                        .append(" al ")
                        .append(blockEntity.getBlockEndDate().format(formatter))
                        .append("\n");
            }
        }

        if (stringBuilder.length() > 0) {
            status.available = false;
            status.message = "No se puede completar el bloqueo. Existen conflictos:\n" + stringBuilder;
            return status;
        }

        createBlock(roomUuid, UUID.fromString(uuidOwner), startDate, endDate, blockType, reason);

        status.available = true;
        status.message = "Nuevo bloqueo establecido:\n" +
                "- RoomUuid: " + uuid + "\n" +
                "- Inicio: " + startDate.format(formatter) + "\n" +
                "- Fin: " + endDate.format(formatter);

        return status;
    }

    @Override
    public BlockDTO createBlock(UUID uuidRoom, UUID uuidOwner, LocalDate blockStartDate, LocalDate blockEndDate, String blockType, String reason) {

        BlockEntity blockEntity = new BlockEntity();
        blockEntity.setUuidRoom(uuidRoom);
        blockEntity.setUuidOwner(uuidOwner);
        blockEntity.setBlockStartDate(blockStartDate);
        blockEntity.setBlockEndDate(blockEndDate);
        blockEntity.setBlockType(blockType);
        blockEntity.setReason(reason);

        blockRepository.save(blockEntity);

        return null;
    }
}