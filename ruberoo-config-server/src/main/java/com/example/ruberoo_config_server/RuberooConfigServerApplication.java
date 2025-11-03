package com.example.ruberoo_config_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class RuberooConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RuberooConfigServerApplication.class, args);
	}

}
