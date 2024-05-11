package org.acme.flow.items;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import mock.MockOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SendEmailToClientFlowItemTest {

    private SendEmailToClientFlowItem flowItem;
    private Mailer mailerMock;
    private Template orderConfirmationTemplateMock;

    @BeforeEach
    void setUp() {
        mailerMock = Mockito.mock(Mailer.class);
        orderConfirmationTemplateMock = Mockito.mock(Template.class);
        flowItem = new SendEmailToClientFlowItem(mailerMock, orderConfirmationTemplateMock);
    }

    @Test
    void testSendEmail_Success() {
        var kafkaOrderDTO = MockOrder.buildKafkaOrderPayload();
        kafkaOrderDTO.getOrderDTO().setOrderOwnerEmail("test@example.com");

        Mockito.when(orderConfirmationTemplateMock.data(Mockito.anyMap())).thenReturn(Mockito.mock(TemplateInstance.class));
        Mockito.when(orderConfirmationTemplateMock.data(Mockito.anyMap()).render()).thenReturn("<html><body>Test HTML</body></html>");

        this.flowItem.sendEmail(kafkaOrderDTO);

        ArgumentCaptor<Mail> mailCaptor = ArgumentCaptor.forClass(Mail.class);
        Mockito.verify(mailerMock).send(mailCaptor.capture());
        Mail sentMail = mailCaptor.getValue();

        assertEquals("Notification-service: News of your order", sentMail.getSubject());
        assertEquals("<html><body>Test HTML</body></html>", sentMail.getHtml());
    }
}