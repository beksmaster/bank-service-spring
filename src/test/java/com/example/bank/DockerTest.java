package com.example.bank;

import org.junit.jupiter.api.Test;
import org.testcontainers.DockerClientFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DockerTest {

    @Test
    void testDocker() {
        var version = DockerClientFactory.instance()
                .client()
                .versionCmd()
                .exec();

        assertNotNull(version, "Docker version response should not be null");
    }
}