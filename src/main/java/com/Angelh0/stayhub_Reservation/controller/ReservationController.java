package com.Angelh0.stayhub_Reservation.controller;

import com.Angelh0.stayhub_Reservation.Service.BlockService;
import com.Angelh0.stayhub_Reservation.Service.ReservationService;
import com.Angelh0.stayhub_Reservation.dto.BlockDTO;
import com.Angelh0.stayhub_Reservation.dto.ReservationDTO;
import org.springframework.cglib.core.Block;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ReservationController {

    private final ReservationService reservationService;
    private final BlockService blockService;

    public ReservationController(ReservationService reservationService, BlockService blockService) {
        this.reservationService = reservationService;
        this.blockService = blockService;
    }

    @PostMapping("/createReservation/{uuidRoom}")
    public ReservationDTO createReservation(@PathVariable UUID uuidRoom, Authentication authentication) {
        UUID uuidUser = UUID.fromString(authentication.getPrincipal().toString());
        return reservationService.createReservation(uuidRoom, uuidUser);
    }

    @GetMapping("/myReservation/user")
    public List<ReservationDTO> getMyReservation(Authentication authentication) {
        UUID uuidUser = UUID.fromString(authentication.getPrincipal().toString());
        return reservationService.getMyReservation(uuidUser);
    }

    @PatchMapping("/cancelReservation/{uuid}")
    public ReservationDTO cancelReservation(@PathVariable UUID uuid, Authentication authentication) {
        UUID uuidUser = UUID.fromString(authentication.getPrincipal().toString());
        return reservationService.cancelReservation(uuid, uuidUser);
    }

    @GetMapping("/myReservation/owner")
    public List<ReservationDTO> getOwnerReservation(Authentication authentication) {
        UUID uuidUser = UUID.fromString(authentication.getPrincipal().toString());
        return reservationService.getOwnerReservation(uuidUser);
    }

    @GetMapping("/getBlock")
    public List<BlockDTO> getBlock(Authentication authentication) {
        UUID uuidOwner = UUID.fromString(authentication.getPrincipal().toString());
        return blockService.getBlock(uuidOwner);
    }

    @PatchMapping("/cancelBlock/{uuidBlock}")
    public List<BlockDTO> cancelBlock(@PathVariable UUID uuidBlock, Authentication authentication) {
        UUID uuidOwner = UUID.fromString(authentication.getPrincipal().toString());
        return blockService.cancelBlock(uuidBlock, uuidOwner);
    }
}