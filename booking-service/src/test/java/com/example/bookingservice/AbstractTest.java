package com.example.bookingservice;

import com.example.basedomain.RoomReservationEvent;
import com.example.bookingservice.aop.SendEventAfter;
import com.example.bookingservice.model.RoleType;
import com.example.bookingservice.model.User;
import com.example.bookingservice.repository.UserRepository;
import com.example.bookingservice.service.KafkaMessagePublisher;
import com.example.bookingservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Properties;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles({"test"})
@Testcontainers
public abstract class AbstractTest implements PostgreBaseTest {
    static  {
        POSTGRE_SQL_CONTAINER.start();
    }

    @Autowired
    protected SendEventAfter sendEventAfter;
    @Autowired
    protected UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public static void beforeAll() {
        POSTGRE_SQL_CONTAINER.withEnv("spring.datasource.url", (p) -> POSTGRE_SQL_CONTAINER.getJdbcUrl());
        POSTGRE_SQL_CONTAINER.withEnv("spring.datasource.username", (p) -> POSTGRE_SQL_CONTAINER.getUsername());
        POSTGRE_SQL_CONTAINER.withEnv("spring.datasource.password", (p) -> POSTGRE_SQL_CONTAINER.getPassword());

    }


    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    private void setUp() {
        User userAdmin = new User();
        userAdmin.setEmail("admin@admin.ru");
        userAdmin.setPassword("adminpassword");
        userAdmin.setUserName("Alex");
        userAdmin.setRoles(new String[]{RoleType.ROLE_ADMIN.name()});
        userService.save(userAdmin);

        User userUser = new User();
        userUser.setEmail("roy@user.ru");
        userUser.setPassword("userpassword");
        userUser.setUserName("Roy");
        userUser.setRoles(new String[]{RoleType.ROLE_USER.name()});
        userService.save(userUser);
    }

    @AfterEach
    private void tearDown() {
        userRepository.deleteAll();
    }

    protected ResultActions post(String path, Object pathVariable, Object object) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(path, pathVariable)
                .content(objectMapper.writeValueAsString(object))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    protected ResultActions post(String path, Object object) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(path)
                .content(objectMapper.writeValueAsString(object))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    protected ResultActions post(String path, String pathParamName, String pathParam, Object object) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(path).param(pathParamName, pathParam)
                .content(objectMapper.writeValueAsString(object))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

}
