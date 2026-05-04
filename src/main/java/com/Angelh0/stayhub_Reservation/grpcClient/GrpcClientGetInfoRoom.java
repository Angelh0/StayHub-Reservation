package com.Angelh0.stayhub_Reservation.grpcClient;

import com.Angelh0.stayhub_Reservation.dto.ReservationDTO;
import com.roomServiceGrpc.grpc.RoomRequest;
import com.roomServiceGrpc.grpc.RoomResponse;
import com.roomServiceGrpc.grpc.RoomServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GrpcClientGetInfoRoom {

    private final ManagedChannel channel =
            NettyChannelBuilder
                    .forTarget("stayhub-accommodation:9090")
                    .usePlaintext()
                    .build();

    private final RoomServiceGrpc.RoomServiceBlockingStub stub =
            RoomServiceGrpc.newBlockingStub(channel);

    public ReservationDTO getInfoRoom(UUID uuidRoom) {
        RoomRequest request = RoomRequest.newBuilder()
                .setUuidRoom(uuidRoom.toString())
                .build();

        RoomResponse response = stub.getInfoRoom(request);

        ReservationDTO dto = new ReservationDTO();
        dto.setUuidOwner(UUID.fromString(response.getUuidOwner()));
        dto.setUuidRoom(UUID.fromString(response.getUuidRoom()));
        dto.setPrice(response.getPrice());
        dto.setType(response.getType());
        dto.setUuidAccommodation(UUID.fromString(response.getUuidAccommodation()));
        dto.setNameAccommodation(response.getNameAccommodation());
        return dto;
    }
}
