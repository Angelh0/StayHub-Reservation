package com.Angelh0.stayhub_Reservation.grpcServiceImpl;

import com.Angelh0.stayhub_Reservation.Service.BusinessService;
import com.Angelh0.stayhub_Reservation.Service.ReservationService;
import com.Angelh0.stayhub_Reservation.dto.StatusCheckValue;
import com.checkAvailability.grpc.ReservationAvailabilityServiceGrpc;
import com.checkAvailability.grpc.ReservationStatusChangeRequest;
import com.checkAvailability.grpc.ReservationStatusChangeResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@GrpcService
public class GrpcCheckRoomStatusChangeImpl extends ReservationAvailabilityServiceGrpc.ReservationAvailabilityServiceImplBase{

    @Autowired
    private BusinessService businessService;

    @Override
    public void checkRoomStatusChange(ReservationStatusChangeRequest request, StreamObserver<ReservationStatusChangeResponse> responseObserver) {

        StatusCheckValue changeStatus = businessService.isCheckStatus(request.getRoomUuid(), request.getUuidOwner(), LocalDate.parse(request.getStartDate()), LocalDate.parse(request.getEndDate()));

        ReservationStatusChangeResponse response = ReservationStatusChangeResponse.newBuilder()
                .setAvailable(changeStatus.available)
                .setRoomUuid(request.getRoomUuid())
                .setMessage(changeStatus.message)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
