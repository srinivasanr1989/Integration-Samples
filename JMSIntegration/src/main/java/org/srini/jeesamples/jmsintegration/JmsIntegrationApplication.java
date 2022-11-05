package org.srini.jeesamples.jmsintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.SpringServletContainerInitializer;

@SpringBootApplication
@EnableJms
@EnableScheduling
public class JmsIntegrationApplication extends SpringServletContainerInitializer {

	public static void main(String[] args) {
		SpringApplication.run(JmsIntegrationApplication.class, args);
	}

}
