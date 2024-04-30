package org.acme.flow;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.acme.dto.OrderDTO;

@RequiredArgsConstructor
@Singleton
public class CreateOrderFlow {

    @Transactional
    public void createOrder (OrderDTO orderDTO) {
    }

}
