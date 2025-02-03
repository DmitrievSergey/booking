package com.example.bookingservice.aop;

import com.example.basedomain.RoomReservation;
import com.example.basedomain.RoomReservationEvent;
import com.example.basedomain.UserRegistration;
import com.example.basedomain.UserRegistrationEvent;
import com.example.bookingservice.dto.reservation.response.ResponseReservation;
import com.example.bookingservice.dto.user.response.ResponseUser;
import com.example.bookingservice.exception.EntityAlreadyExistsException;
import com.example.bookingservice.service.KafkaMessagePublisher;
import com.example.bookingservice.utils.AppMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;


@Component
@Slf4j
@Aspect
@RequiredArgsConstructor
public class SendEventAfter {

    private final KafkaMessagePublisher kafkaMessagePublisher;


    @Around("@annotation(SendEvent)")
    public Object sendEvent(ProceedingJoinPoint joinPoint) {
        ResponseEntity entity = null;
        if (joinPoint.getSignature().getName().contains("createUser")) {
            try {
                entity = (ResponseEntity) joinPoint.proceed();
                sendUserRegisteredEvent(entity);
            } catch (EntityAlreadyExistsException exception) {
                log.info("Зашли в кэтч");
                throw new EntityAlreadyExistsException(
                                exception.getLocalizedMessage()
                );
            } catch (KafkaException e) {
                e.printStackTrace();
                throw new KafkaException(e.getLocalizedMessage());
            } catch (Throwable e) {
                e.printStackTrace();

            }


        } else if (joinPoint.getSignature().getName().contains("reserveRoom")) {
            try {
                entity = (ResponseEntity) joinPoint.proceed();
                sendReserveRoomEvent(entity);
            } catch (EntityAlreadyExistsException exception) {
                throw new EntityAlreadyExistsException(
                        exception.getLocalizedMessage()
                );
            } catch (KafkaException e) {
                e.printStackTrace();
                throw new KafkaException(e.getLocalizedMessage());
            }catch (Throwable e) {
                e.printStackTrace();
            }
        }

        return entity;
    }

    private void sendReserveRoomEvent(ResponseEntity entity) {
        ResponseReservation reservation = (ResponseReservation) entity.getBody();
        RoomReservationEvent event = new RoomReservationEvent();
        log.info("User id {}", reservation.getUser().getId());
        log.info("Room id {}", reservation.getRoom().getId());
        event.setRoomReservation(new RoomReservation(
                reservation.getUser().getId(),
                reservation.getRoom().getId(),
                reservation.getStartDate(),
                reservation.getEndDate()));
        kafkaMessagePublisher.sendRoomReservationEvent(event);
        log.info("Message sent to kafka");
    }

    private void sendUserRegisteredEvent(ResponseEntity entity) {
        ResponseUser user = (ResponseUser) entity.getBody();
        UserRegistrationEvent event = new UserRegistrationEvent();
        event.setUserRegistration(new UserRegistration(user.getId()));
        kafkaMessagePublisher.sendUserRegistrationEvent(event);
        log.info("Message sent to kafka");
    }

}
