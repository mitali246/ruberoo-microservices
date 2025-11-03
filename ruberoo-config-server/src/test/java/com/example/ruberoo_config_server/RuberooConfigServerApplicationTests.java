package com.example.ruberoo_config_server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = { "spring.cloud.config.enabled=false" })
class RuberooConfigServerApplicationTests {

	@Test
	void contextLoads() {
	}

}
