package com.ruberoo.api_gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = { "spring.cloud.config.enabled=false", "ruberoo.jwt.secret-key=dummy-secret-key-for-testing" })
class ApiGatewayApplicationTests {

	@Test
	void contextLoads() {
	}

}
