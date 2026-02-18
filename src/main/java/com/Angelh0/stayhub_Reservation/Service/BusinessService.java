package com.Angelh0.stayhub_Reservation.Service;

import com.Angelh0.stayhub_Reservation.dto.BlockDTO;
import com.Angelh0.stayhub_Reservation.dto.StatusCheckValue;

import java.time.LocalDate;
import java.util.UUID;

public interface BusinessService {

    StatusCheckValue isCheckStatus(String uuid, String uuidOwner, LocalDate startDate, LocalDate endDate);
    BlockDTO createBlock(UUID uuidRoom, UUID uuidOwner, LocalDate blockStartDate, LocalDate blockEndDate);
}
