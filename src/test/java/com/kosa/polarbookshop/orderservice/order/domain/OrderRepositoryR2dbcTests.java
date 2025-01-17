package com.kosa.polarbookshop.orderservice.order.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.kosa.polarbookshop.orderservice.config.DataConfig;

import reactor.test.StepVerifier;

@DataR2dbcTest // R2DBC 컴포넌트에 집중하는 테스트 클래스임을 나타낸다
@Import(DataConfig.class) // 감사를 활성화하기 위한 R2DBC 설정을 임포트한다
@Testcontainers // 테스트 컨테이너의 자동 시작과 중지를 활성화한다
public class OrderRepositoryR2dbcTests {

    @Container // 테스트을 위한 Mariadb 서버 컨테이너를 식별한다
    static MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.11"));

    @Autowired
    private OrderRepository orderRepository;

    @DynamicPropertySource // 테스트 mariadb 서버 인스턴스에 연결하도록 R2DBC와 플라이웨이 설정을 변경한다
    static void mariadbProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.r2dbc.url", OrderRepositoryR2dbcTests::r2dbcUrl);
        registry.add("spring.r2dbc.username", mariaDBContainer::getUsername);
        registry.add("spring.r2dbc.password", mariaDBContainer::getPassword);
        registry.add("spring.flyway.url", mariaDBContainer::getJdbcUrl);
    }

    private static String r2dbcUrl() {
        return mariaDBContainer.getJdbcUrl().replace("jdbc:", "r2dbc:");
    }

    @Test
    void 거부된_주문_생성_테스트() {
        var rejectedOrder = OrderService.buildRejectedOrder("1234567890", 3);
        StepVerifier.create(orderRepository.save(rejectedOrder)) // StepVerifier 객체를 orderRepository가 반환하는 객체로 초기화 한다
                .expectNextMatches(order -> order.status().equals(OrderStatus.REJECTED)) // 반환된 주문이 올바른 상태를 가지고 있는지 확인한다
                .verifyComplete();// 리엑티브 스트림이 성공적으로 완료됐는지 확인한다
    }
}
