package org.acme.flow.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.acme.dto.KafkaOrderDTO;
import org.acme.dto.RequestProductDTO;
import org.acme.dto.StatusOrderAndStockDTO;
import org.acme.enums.OrderStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ApplicationScoped
public class HttpRequestCommerceServiceFlowItem {

    private final HttpClient httpClient;

    public HttpRequestCommerceServiceFlowItem() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public HttpResponseStatus sendHttpRequest (KafkaOrderDTO kafkaOrderDTO) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            var requestBody = objectMapper.writeValueAsString(buildRequestBody(kafkaOrderDTO));

            var response = this.httpClient.send(getHttpRequest(kafkaOrderDTO, requestBody), HttpResponse.BodyHandlers.ofString());
            var httpStatus = HttpResponseStatus.valueOf(response.statusCode());

            if (httpStatus.code() == 204) {
                log.info("Order Status and Stock Updated Sucessfull.");
            } else {
                log.error("Error when Update Order Status and Stock.");
            }

            return httpStatus;
        } catch (IOException e) {
            log.error("Error when Update Order Status and Stock.");
        } catch (InterruptedException e) {
            log.error("Error when Update Order Status and Stock.");
            Thread.currentThread().interrupt();
        }

        return null;
    }

    private static HttpRequest getHttpRequest(KafkaOrderDTO kafkaOrderDTO, String requestBody) {
        return HttpRequest.newBuilder()
                .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create("http://localhost:8080/order/" + kafkaOrderDTO.getOrderId()))
                .build();
    }

    private static StatusOrderAndStockDTO buildRequestBody(KafkaOrderDTO kafkaOrderDTO) {
        if(kafkaOrderDTO.getConfirmedProducts().isEmpty()) {
            return StatusOrderAndStockDTO.builder().orderStatus(OrderStatus.CANCELED.name()).build();
        } else {
            List<RequestProductDTO> requestProductDTOList = new ArrayList<>();

            kafkaOrderDTO.getConfirmedProducts().forEach(product -> {
                requestProductDTOList.add(
                        RequestProductDTO.builder()
                                .name(product.getName())
                                .category(product.getCategory())
                                .amount(product.getAmount())
                                .build());
            });

            return StatusOrderAndStockDTO.builder().orderStatus(OrderStatus.CONFIRMED.name()).productList(requestProductDTOList).build();
        }
    }
}
