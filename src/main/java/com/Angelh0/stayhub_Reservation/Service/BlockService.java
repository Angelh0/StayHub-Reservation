package com.Angelh0.stayhub_Reservation.Service;

import com.Angelh0.stayhub_Reservation.dto.BlockDTO;
import com.Angelh0.stayhub_Reservation.dto.ReservationDTO;
import org.springframework.cglib.core.Block;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BlockService {
    BlockDTO createBlock(UUID uuidRoom, LocalDate blockStartDate, LocalDate blockEndDate);
    BlockDTO cancelBlock(UUID uuidRoom, UUID uuidOwner);
    List<BlockDTO> getBlock(UUID uuidRoom, UUID uuidOwner);
}
