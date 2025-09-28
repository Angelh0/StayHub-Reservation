package com.Angelh0.stayhub_Reservation.grpcClient;

import com.Angelh0.stayhub_Reservation.dto.ReservationDTO;
import com.roomServiceGrpc.grpc.ReservationRequest;
import com.roomServiceGrpc.grpc.RoomResponse;
import com.roomServiceGrpc.grpc.RoomServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class GrpcClientGetInfoRoom {

    private final ManagedChannel channel =
            NettyChannelBuilder.forTarget("localhost:9090")
                    .usePlaintext()
                    .build();

    private final RoomServiceGrpc.RoomServiceBlockingStub stub =
            RoomServiceGrpc.newBlockingStub(channel);

    public ReservationDTO getInfoRoom(UUID uuidRoom) {
        ReservationRequest request = ReservationRequest.newBuilder()
                .setUuidRoom(uuidRoom.toString())
                .build();

        RoomResponse response = stub.getInfoRoom(request);

        // Construimos el DTO con lo que devuelve el servidor
        ReservationDTO dto = new ReservationDTO();
        dto.setUuidRoom(UUID.fromString(response.getUuidRoom()));
        dto.setPrice(response.getPrice());

        return dto;
    }
}
