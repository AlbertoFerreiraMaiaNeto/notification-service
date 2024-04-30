package org.acme.kafka;


import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.KafkaOrderDTO;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class KafkaOrderConsumer {

    @Incoming("orders-in")
    public void receive(KafkaOrderDTO kafkaOrderDTO) {
        System.out.println(kafkaOrderDTO.toString());
    }


}