package com.Angelh0.stayhub_Reservation.Service;

import com.Angelh0.stayhub_Reservation.dto.BlockDTO;

import java.time.LocalDate;
import java.util.UUID;

public interface BlockService {
    BlockDTO createBlock(UUID uuidRoom, LocalDate blockStartDate, LocalDate blockEndDate);
}
