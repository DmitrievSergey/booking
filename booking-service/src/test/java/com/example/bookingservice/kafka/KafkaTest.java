package com.example.bookingservice.kafka;

import com.example.basedomain.RoomReservationEvent;
import com.example.bookingservice.AbstractTest;
import com.example.bookingservice.KafkaBaseTest;
import com.example.bookingservice.dto.reservation.request.RequestReserveRoom;
import com.example.bookingservice.exception.EntityAlreadyExistsException;
import com.example.bookingservice.service.KafkaMessagePublisher;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class KafkaTest extends AbstractTest implements KafkaBaseTest {

    private String roomId = "73afa3b2-067a-4b68-acc1-3cfed494fe8a";


    private static KafkaMessageListenerContainer<String, ?> clientConsumer;

    private static final BlockingQueue<ConsumerRecord<String, ?>> CLIENT_CONSUMER_RECORDS =
            new LinkedBlockingQueue<>(1);

    @BeforeAll
    static void setUp() {
        final var clientConsumerProps = new HashMap<String, Object>();
        clientConsumerProps.put(BOOTSTRAP_SERVERS_CONFIG, KAFKA_CONTAINER.getBootstrapServers());
        clientConsumerProps.put(GROUP_ID_CONFIG, "reservation-group-id");
        clientConsumerProps.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        clientConsumerProps.put(ALLOW_AUTO_CREATE_TOPICS_CONFIG, "true");
        clientConsumerProps.put(MAX_POLL_RECORDS_CONFIG, "1");
        clientConsumerProps.put(ENABLE_AUTO_COMMIT_CONFIG, "false");
        clientConsumerProps.put(MAX_POLL_INTERVAL_MS_CONFIG, "60000");


        final var deserializer = new JsonDeserializer<RoomReservationEvent>();
        deserializer.addTrustedPackages("*");
        final var clientConsumerFactory = new DefaultKafkaConsumerFactory<>(
                clientConsumerProps,
                new StringDeserializer(),
                deserializer
        );

        final var clientConsumerContainerProperties = new ContainerProperties("room-reservation-topic");
        clientConsumerContainerProperties.setAckMode(MANUAL);
        clientConsumer = new KafkaMessageListenerContainer<>(clientConsumerFactory, clientConsumerContainerProperties);

        clientConsumer.setupMessageListener(
                (AcknowledgingMessageListener<String, ?>) (data, acknowledgment) -> {
                    try {
                        // поток, обслуживающий клиентский консюмер будет заблокирован, пока в блокирующей очереди
                        // есть хотя бы один необработанный ивент
                        CLIENT_CONSUMER_RECORDS.put(data);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    requireNonNull(acknowledgment).acknowledge();
                }

        );
        clientConsumer.start();
    }

    @AfterAll
    static void stop() {
        clientConsumer.stop();
    }

    @ParameterizedTest()
    @CsvSource({"1977-02-10, 1977-02-28",
            "1977-04-01, 1977-04-05"})
    @DisplayName("Test send reservation with non intersection intervals event to kafka by admin")
    @Sql("classpath:db/createRoom.sql")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl"
            , value = "Alex"
            , setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenAdminCreateRoomReservationWithNonIntersectedIntervals_ThenEventSendToKafka(LocalDate dateStart, LocalDate dateEnd) throws Exception {


        String userId = userService.findByUserName("Alex").getId();
        post("/api/v1/reservation/{roomId}", roomId, new RequestReserveRoom(
                dateStart
                , dateEnd
        ));

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(15, SECONDS)
                .untilAsserted(() -> {
                    final var response = Optional.ofNullable(CLIENT_CONSUMER_RECORDS.poll());

                    RoomReservationEvent reservation = (RoomReservationEvent) response.get().value();
                    assertEquals(roomId, reservation.getRoomReservation().getRoomId());
                    assertEquals(userId, reservation.getRoomReservation().getUserId());
                    assertEquals(dateStart, reservation.getRoomReservation().getStartDate());
                    assertEquals(dateEnd, reservation.getRoomReservation().getEndDate());
                });
    }

    @DisplayName("Test even does not send when create interval with intersect with existing interval  by admin")
    @Sql("classpath:db/createReservation.sql")
    @ParameterizedTest()
    @CsvSource({"1977-03-01, 1977-03-05"
            , "1977-02-28, 1977-03-03"
            , "1977-03-02, 1977-03-06"
            , "1977-03-02, 1977-03-04"
            , "1977-02-28, 1977-03-07"
            , "1977-05-01, 1977-05-09"})
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl"
            , value = "Alex"
            , setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenAdminCreateRoomReservationWithExistsDate_ThenEventDoesNotSend(LocalDate startDate, LocalDate endDate) throws Exception {

        post("/api/v1/reservation/{roomId}", roomId, new RequestReserveRoom(
                startDate
                , endDate
        ))
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityAlreadyExistsException))
                .andExpect(result -> assertEquals("Интервал with name "+ startDate +" - "+ endDate +" already exists", result.getResolvedException().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Интервал with name "+ startDate +" - "+ endDate +" already exists"));

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(15, SECONDS)
                .untilAsserted(() -> {
                    final var response = Optional.ofNullable(CLIENT_CONSUMER_RECORDS.poll());
                    assertFalse(response.isPresent());
                });
    }
}
