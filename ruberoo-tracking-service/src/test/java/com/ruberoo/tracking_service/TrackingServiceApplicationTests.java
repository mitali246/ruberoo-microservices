package com.ruberoo.tracking_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = { "spring.cloud.config.enabled=false" })
class TrackingServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
