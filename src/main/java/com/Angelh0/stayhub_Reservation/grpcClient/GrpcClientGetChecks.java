package com.Angelh0.stayhub_Reservation.grpcClient;

import com.Angelh0.stayhub_Reservation.dto.ReservationDTO;
import com.checkServiceGrpc.grpc.CheckRequest;
import com.checkServiceGrpc.grpc.CheckResponse;
import com.checkServiceGrpc.grpc.CheckServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class GrpcClientGetChecks {

    private final ManagedChannel channel =
            NettyChannelBuilder.forTarget("localhost:9090")
                    .usePlaintext()
                    .build();

    private final CheckServiceGrpc.CheckServiceBlockingStub stub =
            CheckServiceGrpc.newBlockingStub(channel);

    public ReservationDTO getCheckRoom() {
        CheckRequest request = CheckRequest.newBuilder()
                .setId(1)
                .build();

        CheckResponse response = stub.getCheckRoom(request);

        ReservationDTO dto = new ReservationDTO();
        dto.setCheckIn(LocalDate.parse(response.getCheckIn()));
        dto.setCheckOut(LocalDate.parse(response.getCheckOut()));

        return dto;
    }
}