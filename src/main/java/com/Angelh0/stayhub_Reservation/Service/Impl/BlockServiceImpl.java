package com.Angelh0.stayhub_Reservation.Service.Impl;

import com.Angelh0.stayhub_Reservation.Service.BlockService;
import com.Angelh0.stayhub_Reservation.converter.BlockConverter;
import com.Angelh0.stayhub_Reservation.dto.BlockDTO;
import com.Angelh0.stayhub_Reservation.entity.BlockEntity;
import com.Angelh0.stayhub_Reservation.entity.ReservationEntity;
import com.Angelh0.stayhub_Reservation.repository.BlockRepository;
import com.Angelh0.stayhub_Reservation.repository.ReservationRepository;
import org.springframework.cglib.core.Block;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BlockServiceImpl implements BlockService {

    private final ReservationRepository reservationRepository;
    private final BlockRepository blockRepository;
    private final BlockConverter blockConverter;

    public BlockServiceImpl(ReservationRepository reservationRepository1, BlockRepository blockRepository, BlockConverter blockConverter) {
        this.reservationRepository = reservationRepository1;
        this.blockRepository = blockRepository;
        this.blockConverter = blockConverter;
    }

    @Override
    public List<BlockDTO> cancelBlock(UUID uuidBlock, UUID uuidOwner) {

        List<BlockEntity> blockEntityList = blockRepository.findByUuidAndUuidOwner(uuidBlock, uuidOwner);

        if (blockEntityList.isEmpty()) {
            return null;
        }

        List<BlockDTO> blockDTOS = new ArrayList<>();

        for (BlockEntity blockEntity : blockEntityList) {
            if (blockEntity.getUuid().equals(uuidBlock)) {
                blockRepository.delete(blockEntity);
                blockDTOS.add(blockConverter.convertToDTO(blockEntity));
            }
        }
        return blockDTOS;
    }

    @Override
    public List<BlockDTO> getBlock(UUID uuidOwner) {
        List<BlockEntity> blockEntityList = blockRepository.findByUuidOwner(uuidOwner);

        if (blockEntityList.isEmpty()) {
            return new ArrayList<>();
        }

        List<BlockDTO> blockDTOS = new ArrayList<>();
        for (BlockEntity blockEntity : blockEntityList) {
            blockDTOS.add(blockConverter.convertToDTO(blockEntity));
        }
        return blockDTOS;
    }

}
