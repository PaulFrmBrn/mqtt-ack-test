package com.paulfrmbrn;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Path("/hello")
public class GreetingResource {

    private static final Logger log = LoggerFactory.getLogger(GreetingResource.class);

    @Inject
    @Channel("test-topic-mqtt")
    Emitter<String> mqttEmitter;

    @Inject
    @Channel("test-topic-amqp")
    Emitter<String> amqpEmitter;

    @GET
    @Path("/mqtt/payload")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloMqttPayload() {
        var message = "Hello";
        log.info("mqtt: message prepared");
        CompletionStage<Void> send = mqttEmitter.send("hello");
        log.info("mqtt: message emitted");
        send.toCompletableFuture().join();
        log.info("mqtt: message acked");
        return message;
    }

    @GET
    @Path("/mqtt/message")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloMqttMessage() {
        var message = Message.of("Hello").withAck(() -> {
            log.info("mqtt: message acked");
            return CompletableFuture.completedStage(null);
        });
        log.info("mqtt: message prepared");
        mqttEmitter.send(message);
        log.info("mqtt: message emitted");

        return message.getPayload();
    }

    @GET
    @Path("/amqp/payload")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloAmqpPayload() {
        var message = "Hello";
        log.info("amqp: message prepared");
        CompletionStage<Void> send = amqpEmitter.send("hello");
        log.info("amqp: message emitted");
        send.toCompletableFuture().join();
        log.info("amqp: message acked");
        return message;
    }

    @GET
    @Path("/amqp/message")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloAmqpMessage() {
        var message = Message.of("Hello").withAck(() -> {
            log.info("amqp: message acked");
            return CompletableFuture.completedStage(null);
        });
        log.info("amqp: message prepared");
        amqpEmitter.send(message);
        log.info("amqp: message emitted");
        return message.getPayload();
    }

}