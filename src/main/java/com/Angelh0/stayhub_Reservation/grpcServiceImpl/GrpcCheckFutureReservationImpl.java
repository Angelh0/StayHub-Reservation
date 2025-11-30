package com.Angelh0.stayhub_Reservation.grpcServiceImpl;

import com.Angelh0.stayhub_Reservation.Service.ReservationService;
import com.checkAvailability.grpc.ReservationCheckServiceGrpc;
import com.checkAvailability.grpc.ReservationRequest;
import com.checkAvailability.grpc.ReservationResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class GrpcCheckFutureReservationImpl extends ReservationCheckServiceGrpc.ReservationCheckServiceImplBase {

    @Autowired
    private ReservationService reservationService;

    @Override
    public void checkFutureReservation(ReservationRequest request, StreamObserver<ReservationResponse> responseObserver) {

        boolean futureReservation = reservationService.isFutureReservation(request.getRoomUuid());

        ReservationResponse reservationResponse = ReservationResponse.newBuilder()
                .setRoomUuid(request.getRoomUuid())
                .setAvailable(futureReservation)
                .build();

        responseObserver.onNext(reservationResponse);
        responseObserver.onCompleted();
    }
}