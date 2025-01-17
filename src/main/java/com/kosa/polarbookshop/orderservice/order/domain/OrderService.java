package com.kosa.polarbookshop.orderservice.order.domain;

import org.springframework.stereotype.Service;

import com.kosa.polarbookshop.orderservice.book.Book;
import com.kosa.polarbookshop.orderservice.config.BookClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final BookClient bookClient;
    private final OrderRepository orderRepository;

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Mono<Order> submitOrder(String isbn, int quantiry) {
        return bookClient.getBookByIsbn(isbn) // 카탈로그 서비스를 호출해 책의 주문 가능성을 확인한다
                .map(book -> buildAcceptedOrder(book, quantiry)) // 책 주문이 가능하면 접수한다
                .defaultIfEmpty(buildRejectedOrder(isbn, quantiry)) // 책이 카탈로그에 존재하지 않으면 주문을 거부한다
                .flatMap(orderRepository::save); // 주문을 (접수 또는 거부상태로) 저장한다
    }

    public static Order buildAcceptedOrder(Book book, int quantiry) {
        return Order.of(book.isbn(), book.title() + "-" + book.author(), book.price(), quantiry, OrderStatus.ACCEPTED);
    }

    public static Order buildRejectedOrder(String isbn, int quantiry) {
        return Order.of(isbn, null, null, quantiry, OrderStatus.REJECTED);
    }

}
