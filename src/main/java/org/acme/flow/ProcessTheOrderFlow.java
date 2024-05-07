package org.acme.flow;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.dto.KafkaOrderDTO;
import org.acme.flow.items.HttpRequestCommerceServiceFlowItem;
import org.acme.flow.items.SendEmailToClientFlowItem;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class ProcessTheOrderFlow {

    private final HttpRequestCommerceServiceFlowItem httpRequestCommerceServiceFlowItem;

    private final SendEmailToClientFlowItem sendEmailToClientFlowItem;

    public void process(KafkaOrderDTO kafkaOrderDTO) {
        try {
            var responseStatus = this.httpRequestCommerceServiceFlowItem.sendHttpRequest(kafkaOrderDTO);

            if(responseStatus.code() == 204) {
                sendEmail(kafkaOrderDTO);
            }

        } catch (Exception e) {
            log.error("Error to process the order. - Cause: ", e);
        }
    }

    private void sendEmail(KafkaOrderDTO kafkaOrderDTO) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                this.sendEmailToClientFlowItem.sendEmail(kafkaOrderDTO);
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
