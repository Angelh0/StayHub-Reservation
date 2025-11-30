package com.Angelh0.stayhub_Reservation.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class BlockDTO {

    private UUID uuid;
    private UUID uuidRoom;

    private LocalDate blockStartDate;
    private LocalDate blockEndDate;
}
