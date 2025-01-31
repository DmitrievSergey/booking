package com.example.statisticservice.listener;

import com.example.basedomain.RoomReservationEvent;
import com.example.basedomain.UserRegistrationEvent;
import com.example.statisticservice.entity.BookingReservationEntity;
import com.example.statisticservice.entity.BookingUserEntity;
import com.example.statisticservice.service.BookingReservationService;
import com.example.statisticservice.service.BookingUserService;
import com.example.statisticservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatisticListener {
    private final BookingUserService userService;
    private final BookingReservationService reservationService;

    @KafkaListener(topics = {"${app.kafka.roomReservation}"}
            , groupId = "${app.kafka.kafkaMessageGroupId}"
            , containerFactory = "concurrentKafkaListenerContainerFactory")
    public void roomReservationConsumer(@Payload RoomReservationEvent message,
            @Header(value = KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        BookingReservationEntity reservation = new BookingReservationEntity(
                UUID.randomUUID().toString(),
                message.getRoomReservation().getUserId(),
                message.getRoomReservation().getRoomId(),
                message.getRoomReservation().getStartDate(),
                message.getRoomReservation().getEndDate()

        );

        reservationService.save(reservation);
    }

    @KafkaListener(topics = {"${app.kafka.userRegistration}"}
            , groupId = "${app.kafka.kafkaMessageGroupId}"
            , containerFactory = "concurrentKafkaListenerContainerFactory")
    public void userRegistrationConsumer(UserRegistrationEvent message,
                       @Header(value = KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        log.info("Hello");
        BookingUserEntity user = new BookingUserEntity(message.getUserRegistration().getUserId());
        log.info("User id {}", user.getId());
        userService.saveUserRegistrationEvent(user);
    }
}
