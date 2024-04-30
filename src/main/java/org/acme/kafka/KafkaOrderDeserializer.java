package org.acme.kafka;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.acme.dto.KafkaOrderDTO;

public class KafkaOrderDeserializer extends ObjectMapperDeserializer<KafkaOrderDTO> {
    public KafkaOrderDeserializer() {
        super(KafkaOrderDTO.class);
    }
}