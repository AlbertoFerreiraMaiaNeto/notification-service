package org.acme.flow.order;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.acme.enums.StatusOrder;
import org.acme.flow.order.items.ValidateProductAndAmountFlowItem;
import org.acme.persistence.dto.KafkaPayloadDTO;
import org.acme.persistence.dto.OrderDTO;
import org.acme.persistence.dto.OrderResponseDTO;
import org.acme.persistence.model.Category;
import org.acme.persistence.model.Order;
import org.acme.persistence.model.OrderProduct;
import org.acme.persistence.model.Product;
import org.acme.persistence.service.OrderService;
import org.acme.persistence.service.ProductService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Singleton
public class CreateOrderFlow {

    private final OrderService orderService;
    private final ProductService productService;
    private final ValidateProductAndAmountFlowItem validateProductAndAmountFlowItem;

    @Transactional
    public OrderResponseDTO createOrder (OrderDTO orderDTO) {
        var productList = this.productService.findAll();

        var kafkaPayload = this.validateProductAndAmountFlowItem.validate(orderDTO, productList);

        var order = Order.builder().orderOwner(orderDTO.getOrderOwner()).orderStatus(StatusOrder.PENDING.name()).build();

        List<OrderProduct> orderProducts = getOrderProducts(order, kafkaPayload);

        order.setOrderProducts(orderProducts);

        this.orderService.save(order);

        return OrderResponseDTO.builder()
                .orderOwner(orderDTO.getOrderOwner())
                .status(StatusOrder.PENDING)
                .productList(kafkaPayload.getConfirmedProducts())
                .build();
    }

    private static List<OrderProduct> getOrderProducts(Order order, KafkaPayloadDTO kafkaPayload) {
        List<OrderProduct> orderProducts = new ArrayList<>();

        kafkaPayload.getConfirmedProducts().forEach(product -> {
            var categoryEntity = Category.builder().name(product.getCategory()).build();
            var productEntity = Product.builder().name(product.getName()).category(categoryEntity).build();

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(productEntity);
            orderProduct.setAmount(product.getAmount());

            orderProducts.add(orderProduct);
        });

        return orderProducts;
    }
}