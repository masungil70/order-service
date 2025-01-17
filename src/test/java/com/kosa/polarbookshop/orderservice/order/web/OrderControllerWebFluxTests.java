package com.kosa.polarbookshop.orderservice.order.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.kosa.polarbookshop.orderservice.order.domain.Order;
import com.kosa.polarbookshop.orderservice.order.domain.OrderService;
import com.kosa.polarbookshop.orderservice.order.domain.OrderStatus;

import reactor.core.publisher.Mono;

@WebFluxTest(OrderController.class) // OrderController을 대상으로 한 스프링 웹플럭스 컴퍼넌트에 집중하는 테스트 클래스임을 나타낸다
public class OrderControllerWebFluxTests {

    @Autowired
    private WebTestClient webClient; // 웹 클라이언트의 변형으로 Restful 서비스 테스트를 쉽게 하기 위한 기능을 추가로 가지고 있다

    @MockitoBean
    private OrderService orderService; // OrderSerivce의 모의 객체를 스프링 어플리케이션 컨텍스트에 추가한다

    @Test
    void 예약이_불가능할_경우_주문을_거부하는_테스트() {
        var orderRequest = new OrderRequest("1234567890", 3);
        var expectedOrder = OrderService.buildRejectedOrder(orderRequest.isbn(), orderRequest.quantity());

        // orderService 모의 객체빈이 어떻게 동작해야 하는지 지정한다
        // 실제로 orderService.submitOrder() 메소드가 동작하는 것이 아니라
        // orderService.submitOrder() 메소드가 호출되면 리턴은 expectedOrder을 하라고 설정하는 것임
        given(orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity()))
                .willReturn(Mono.just(expectedOrder));

        webClient
                .post()
                .uri("/orders")
                .bodyValue(orderRequest)
                .exchange() // 설정된 URL을 호출한다
                .expectStatus().is2xxSuccessful() // 주문이 성공적으로 생성될 것을 예상한다
                .expectBody(Order.class).value(actualOrder -> {
                    assertThat(actualOrder).isNotNull();
                    assertThat(actualOrder.status()).isEqualTo(OrderStatus.REJECTED);
                });
    }

}
