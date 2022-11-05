package org.srini.jeesamples.jmsintegration.client;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JMSProducerConsumerClient {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private JmsTemplate jmsTemplateWithBeanDestinationResolver;

	private final Random random = new Random();

	private static final Logger LOGGER = LoggerFactory.getLogger(JMSProducerConsumerClient.class);

	@Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
	public void productsMessageProducer() {
		UUID random = UUID.randomUUID();
		LOGGER.info("Sent to products queue: {}", random.toString());
		jmsTemplate.convertAndSend("products", random.toString());
	}

	@JmsListener(containerFactory = "jmsListenerContainerFactory", destination = "products")
	public void productsMessageConsumer(Message<String> message) {
		LOGGER.info("Received from products queue: {}", message.getPayload());
	}

	@Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
	public void pricesMessageProducer() {
		Double randomPrice = random.nextDouble(1000, 10000);
		LOGGER.info("Sent to prices queue: {}", randomPrice);
		jmsTemplateWithBeanDestinationResolver.convertAndSend("priceQueue", "Price:" + randomPrice.toString());
	}

	@JmsListener(containerFactory = "jmsListenerContainerFactoryWithResolver", destination = "priceQueue")
	public void pricesMessageConsumer(Message<String> message) {
		LOGGER.info("Received from prices queue: {}", message.getPayload());
	}
}
