package com.Angelh0.stayhub_Reservation.repository;

import com.Angelh0.stayhub_Reservation.dto.BlockDTO;
import com.Angelh0.stayhub_Reservation.entity.BlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BlockRepository extends JpaRepository<BlockEntity, Long> {
    List<BlockEntity> findBlockByUuidRoom(UUID uuidRoom);

}
