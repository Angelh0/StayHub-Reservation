package com.Angelh0.stayhub_Reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class BlockDTO {

    private UUID uuid;
    private UUID uuidRoom;

    private String blockType;
    private String reason;

    @JsonFormat(pattern="d/M/yyyy")
    private LocalDate blockStartDate;

    @JsonFormat(pattern="d/M/yyyy")
    private LocalDate blockEndDate;
}
