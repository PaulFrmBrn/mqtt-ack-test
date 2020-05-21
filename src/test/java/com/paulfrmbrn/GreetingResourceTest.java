package com.paulfrmbrn;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.connectors.InMemorySink;
import io.smallrye.reactive.messaging.connectors.InMemorySource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingResourceTest {

    @BeforeAll
    public static void switchMyChannels() {
        InMemoryConnector.switchOutgoingChannelsToInMemory("test-topic-mqtt");
        InMemoryConnector.switchOutgoingChannelsToInMemory("test-topic-amqp");
    }

    @AfterAll
    public static void revertMyChannels() {
        InMemoryConnector.clear();
    }

    @Inject
    @Any
    InMemoryConnector connector;

    @Test
    public void testHelloMqttPayloadEndpoint() {

        given()
                .when().get("/hello/mqtt/payload")
                .then()
                .statusCode(200)
                .body(is("Hello"));

    }

    @Test
    public void testHelloMqttMessageEndpoint() {

        given()
          .when().get("/hello/mqtt/message")
          .then()
             .statusCode(200)
             .body(is("Hello"));

    }

    @Test
    public void testHelloAmqpPayloadEndpoint() {

        given()
                .when().get("/hello/amqp/payload")
                .then()
                .statusCode(200)
                .body(is("Hello"));

    }

    @Test
    public void testHelloAmqpMessageEndpoint() {

        given()
                .when().get("/hello/amqp/message")
                .then()
                .statusCode(200)
                .body(is("Hello"));

    }

}