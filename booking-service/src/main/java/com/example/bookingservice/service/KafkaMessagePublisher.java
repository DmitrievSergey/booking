package com.example.bookingservice.service;

import com.example.basedomain.RoomReservationEvent;
import com.example.basedomain.UserRegistrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessagePublisher {
    @Value("${app.kafka.userRegistration}")
    private String userRegistrationTopic;

    @Value("${app.kafka.roomReservation}")
    private String roomReservationTopic;

    private final KafkaTemplate<String, UserRegistrationEvent> template1;
    private final KafkaTemplate<String, RoomReservationEvent> template2;

    public void sendUserRegistrationEvent(UserRegistrationEvent event) {
        CompletableFuture<SendResult<String, UserRegistrationEvent>> result = template1.send(userRegistrationTopic, event);

        result.whenComplete((res, ex) -> {
            if(ex == null) {
                log.info("Sent event {}", event);
                log.info("with offset {}", res.getProducerRecord().toString());
            } else {
                log.info("Unable sent event {}", event);
                log.info("Error {}", ex.getLocalizedMessage());
            }
        });
    }

    public void sendRoomReservationEvent(RoomReservationEvent event) {
        CompletableFuture<SendResult<String, RoomReservationEvent>> result = template2.send(roomReservationTopic, event);

        result.whenComplete((res, ex) -> {
            if(ex == null) {
                log.info("Sent event {}", event);
                log.info("with offset {}", res.getProducerRecord().toString());
            } else {
                log.info("Unable sent event {}", event);
                log.info("Error {}", ex.getLocalizedMessage());
            }
        });
    }
}
