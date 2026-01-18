package com.Angelh0.stayhub_Reservation.Service.Impl;

import com.Angelh0.stayhub_Reservation.Service.BlockService;
import com.Angelh0.stayhub_Reservation.dto.BlockDTO;
import com.Angelh0.stayhub_Reservation.entity.BlockEntity;
import com.Angelh0.stayhub_Reservation.repository.BlockRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class BlockServiceImpl implements BlockService {

    private final BlockRepository blockRepository;

    public BlockServiceImpl(BlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    @Override
    public BlockDTO createBlock(UUID uuidRoom, LocalDate blockStartDate, LocalDate blockEndDate) {

        BlockEntity blockEntity = new BlockEntity();
        blockEntity.setUuidRoom(uuidRoom);
        blockEntity.setBlockStartDate(blockStartDate);
        blockEntity.setBlockEndDate(blockEndDate);

        blockRepository.save(blockEntity);

        return null;
    }
}
