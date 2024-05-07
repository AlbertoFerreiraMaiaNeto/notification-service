package org.acme.flow.items;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.dto.KafkaOrderDTO;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class SendEmailToClientFlowItem {

    @Inject
    private final Mailer mailer;

    @Inject
    private final Template orderConfirmationTemplate;

    public void sendEmail (KafkaOrderDTO kafkaOrderDTO) {
        try {
            var ownerEmail = kafkaOrderDTO.getOrderDTO().getOrderOwnerEmail();
            var subject = "Notification-service: News of your order";

            Map<String, Object> templateData = new HashMap<>();
            templateData.put("kafkaOrderDTO", kafkaOrderDTO);

            TemplateInstance templateInstance = orderConfirmationTemplate.data(templateData);

            String htmlBody = templateInstance.render();

            this.mailer.send(Mail.withHtml(ownerEmail, subject, htmlBody));

            log.info("Order email sent successfully.");
        } catch (Exception e) {
            log.error("Error sending order confirmation email. - Cause: ", e);
        }
    }
}
