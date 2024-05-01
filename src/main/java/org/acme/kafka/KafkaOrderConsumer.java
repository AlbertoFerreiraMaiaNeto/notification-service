package org.acme.kafka;


import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.dto.KafkaOrderDTO;
import org.acme.flow.SendEmailToClientFlow;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class KafkaOrderConsumer {

    private final SendEmailToClientFlow sendEmailToClientFlow;

    @Incoming("orders-in")
    public void receive(KafkaOrderDTO kafkaOrderDTO) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                sendEmailToClientFlow.sendEmail(kafkaOrderDTO);
            } catch (Exception e) {
                throw new CompletionException("Error processing order", e);
            }
        });

        future.exceptionally(ex -> {
            log.error("Error processing order", ex);
            return null;
        });
    }
}