package com.kosa.polarbookshop.orderservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.kosa.polarbookshop.orderservice.OrderServiceApplication;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(classes = OrderServiceApplication.class)
class OrderServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
