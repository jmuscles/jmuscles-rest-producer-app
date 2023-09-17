package com.jmuscles.rest.producer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.jmuscles.rest.producer.config.RestConfPropsForConfigKey;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean("restProducerConfigPropertiesMap")
	@ConfigurationProperties(value = "rest-producer-config")
	public Map<String, RestConfPropsForConfigKey> restProducerConfigProperties() {
		return new HashMap<>();
	}

}
