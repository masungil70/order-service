package com.kosa.polarbookshop.orderservice.order.domain;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Mono<Order> submitOrder(String isbn, int quantiry) {
        return Mono.just(buildRejectedOrder(isbn, quantiry)).flatMap(orderRepository::save);

    }

    public static Order buildRejectedOrder(String isbn, int quantiry) {
        return Order.of(isbn, null, null, quantiry, OrderStatus.REJECTED);
    }

}
