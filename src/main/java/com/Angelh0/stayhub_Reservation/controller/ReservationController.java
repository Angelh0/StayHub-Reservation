package com.Angelh0.stayhub_Reservation.controller;

import com.Angelh0.stayhub_Reservation.Service.BlockService;
import com.Angelh0.stayhub_Reservation.Service.ReservationService;
import com.Angelh0.stayhub_Reservation.dto.BlockDTO;
import com.Angelh0.stayhub_Reservation.dto.ReservationDTO;
import org.springframework.cglib.core.Block;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ReservationDTO createReservation(@PathVariable UUID uuidRoom, Authentication authentication, @RequestHeader("Authorization") String token) throws Exception {

        UUID uuidUser = UUID.fromString(authentication.getPrincipal().toString());
        String payload = new String(java.util.Base64.getUrlDecoder().decode(token.split("\\.")[1]));
        com.fasterxml.jackson.databind.JsonNode json = new com.fasterxml.jackson.databind.ObjectMapper().readTree(payload);
        return reservationService.createReservation(uuidRoom,uuidUser,json.get("firstName").asText(),json.get("lastName").asText(),json.get("sub").asText()
        );
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
    public ResponseEntity<List<BlockDTO>> getBlock(Authentication authentication) {
        UUID uuidOwner = UUID.fromString(authentication.getPrincipal().toString());
        List<BlockDTO> blocks = blockService.getBlock(uuidOwner);
        return new ResponseEntity<>(blocks, HttpStatus.OK);
    }

    @PatchMapping("/cancelBlock/{uuidBlock}")
    public List<BlockDTO> cancelBlock(@PathVariable UUID uuidBlock, Authentication authentication) {
        UUID uuidOwner = UUID.fromString(authentication.getPrincipal().toString());
        return blockService.cancelBlock(uuidBlock, uuidOwner);
    }
}