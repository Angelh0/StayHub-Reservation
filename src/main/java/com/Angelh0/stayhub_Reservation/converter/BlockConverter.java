package com.Angelh0.stayhub_Reservation.converter;

import com.Angelh0.stayhub_Reservation.dto.BlockDTO;
import com.Angelh0.stayhub_Reservation.entity.BlockEntity;
import org.springframework.stereotype.Component;

@Component
public class BlockConverter {

    public BlockDTO convertToDTO(BlockEntity blockEntity)  {
        BlockDTO blockDTO = new BlockDTO();

        blockDTO.setUuid(blockEntity.getUuid());
        blockDTO.setUuidRoom(blockEntity.getUuidRoom());
        blockDTO.setBlockType(blockEntity.getBlockType());
        blockDTO.setReason(blockEntity.getReason());
        blockDTO.setBlockStartDate(blockEntity.getBlockStartDate());
        blockDTO.setBlockEndDate(blockEntity.getBlockEndDate());

        return blockDTO;
    }

    public BlockEntity convertToEntity(BlockDTO blockDTO) {
        BlockEntity blockEntity = new BlockEntity();

        blockEntity.setUuid(blockDTO.getUuid());
        blockEntity.setUuidRoom(blockDTO.getUuidRoom());
        blockEntity.setBlockType(blockDTO.getBlockType());
        blockEntity.setReason(blockDTO.getReason());
        blockEntity.setBlockStartDate(blockDTO.getBlockStartDate());
        blockEntity.setBlockEndDate(blockDTO.getBlockEndDate());

        return blockEntity;
    }
}
