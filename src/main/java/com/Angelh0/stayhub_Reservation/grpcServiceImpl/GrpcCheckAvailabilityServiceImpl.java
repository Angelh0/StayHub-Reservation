package com.Angelh0.stayhub_Reservation.grpcServiceImpl;

import com.Angelh0.stayhub_Reservation.Service.ReservationService;
import com.checkAvailability.grpc.AvailabilityRequest;
import com.checkAvailability.grpc.AvailabilityResponse;
import com.checkAvailability.grpc.RoomAvailability;
import com.checkAvailability.grpc.availabilityServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@GrpcService
public class GrpcCheckAvailabilityServiceImpl extends availabilityServiceGrpc.availabilityServiceImplBase {

    @Autowired
    private ReservationService reservationService;

    @Override
    public void checkAvailability(AvailabilityRequest request, StreamObserver<AvailabilityResponse> responseObserver) {

        AvailabilityResponse.Builder responseBuilder = AvailabilityResponse.newBuilder();


        for (String roomUuid : request.getRoomUuidList()) {
            boolean available = reservationService.isRoomAvailable(roomUuid, LocalDate.parse(request.getCheckIn()), LocalDate.parse(request.getCheckOut()));

            RoomAvailability roomAvailability = RoomAvailability.newBuilder()
                    .setRoomUuid(roomUuid)
                    .setAvailable(available)
                    .build();

            responseBuilder.addRooms(roomAvailability);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
