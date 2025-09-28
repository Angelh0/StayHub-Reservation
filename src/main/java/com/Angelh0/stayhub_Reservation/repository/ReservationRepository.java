package com.Angelh0.stayhub_Reservation.repository;

import com.Angelh0.stayhub_Reservation.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByUuidRoom(UUID uuidRoom);
}