package org.acme.kafka;


import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.dto.KafkaOrderDTO;
import org.acme.flow.ProcessTheOrderFlow;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class KafkaOrderConsumer {

    private final ProcessTheOrderFlow processTheOrderFlow;

    @Incoming("orders-in")
    public void receive(KafkaOrderDTO kafkaOrderDTO) {
        this.processTheOrderFlow.process(kafkaOrderDTO);
    }
}