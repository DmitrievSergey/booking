package com.example.bookingservice;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public interface RedisBaseTest {

    GenericContainer<?> REDIS_CONTAINER =
            new GenericContainer<>(DockerImageName.parse("redis:alpine"))
                    .withExposedPorts(6379);
}
