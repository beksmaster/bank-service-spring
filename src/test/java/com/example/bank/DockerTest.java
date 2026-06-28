package com.example.bank;

import org.junit.jupiter.api.Test;
import org.testcontainers.DockerClientFactory;

class DockerTest {

    @Test
    void testDocker() {
        System.out.println(
                DockerClientFactory.instance()
                        .client()
                        .versionCmd()
                        .exec()
        );
    }
}