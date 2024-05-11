package org.acme.flow;

import io.netty.handler.codec.http.HttpResponseStatus;
import mock.MockOrder;
import org.acme.flow.items.HttpRequestCommerceServiceFlowItem;
import org.acme.flow.items.SendEmailToClientFlowItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class ProcessTheOrderFlowTest {


    private HttpRequestCommerceServiceFlowItem httpRequestCommerceServiceFlowItem;

    private SendEmailToClientFlowItem sendEmailToClientFlowItem;

    private ProcessTheOrderFlow processTheOrderFlow;

    @BeforeEach
    void setUp() {
        this.httpRequestCommerceServiceFlowItem = Mockito.mock(HttpRequestCommerceServiceFlowItem.class);
        this.sendEmailToClientFlowItem = Mockito.mock(SendEmailToClientFlowItem.class);
        this.processTheOrderFlow = new ProcessTheOrderFlow(httpRequestCommerceServiceFlowItem, sendEmailToClientFlowItem);
    }
    @Test
    void process() {
        var kafkaPayload = MockOrder.buildKafkaOrderPayload();
        when(httpRequestCommerceServiceFlowItem.sendHttpRequest(kafkaPayload)).thenReturn(HttpResponseStatus.NO_CONTENT);

        this.processTheOrderFlow.process(kafkaPayload);

        Mockito.verify(sendEmailToClientFlowItem, times(1)).sendEmail(kafkaPayload);
    }
}