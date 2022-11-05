package org.srini.jeesamples.jmsintegration.config;

import java.net.URI;
import java.util.Arrays;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.BeanFactoryDestinationResolver;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JMSConfig {

	@Bean
	public BrokerService createBrokerService() throws Exception {
		BrokerService broker = new BrokerService();
		TransportConnector connector = new TransportConnector();
		connector.setUri(new URI("tcp://localhost:65534"));
		broker.addConnector(connector);
		return broker;
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL("tcp://localhost:65534");
		connectionFactory.setTrustedPackages(Arrays.asList("com.srini"));
		return connectionFactory;
	}

	@Bean
	public PlatformTransactionManager jmsTransactionManager(@Autowired ConnectionFactory connectionFactory) {
		return new JmsTransactionManager(connectionFactory);
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
			@Autowired ConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
		containerFactory.setConnectionFactory(connectionFactory);
		containerFactory.setSessionTransacted(true);
		return containerFactory;
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactoryWithResolver(
			@Autowired ConnectionFactory connectionFactory, @Autowired DestinationResolver resolver) {
		DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
		containerFactory.setConnectionFactory(connectionFactory);
		containerFactory.setDestinationResolver(resolver);
		containerFactory.setSessionTransacted(true);
		return containerFactory;
	}

	@Bean
	public DestinationResolver resolver(@Autowired BeanFactory beanFactory) {
		return new BeanFactoryDestinationResolver(beanFactory);
	}

	@Bean
	public Destination productQueue() {
		Queue queue = new ActiveMQQueue("products");
		return queue;
	}

	@Bean
	public Destination priceQueue() {
		Queue queue = new ActiveMQQueue("prices");
		return queue;
	}

	@Bean
	public JmsTemplate jmsTemplate(@Autowired ConnectionFactory connectionFactory) {
		return new JmsTemplate(connectionFactory);
	}

	@Bean
	public JmsTemplate jmsTemplateWithBeanDestinationResolver(@Autowired ConnectionFactory connectionFactory,
			@Autowired DestinationResolver resolver) {
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		jmsTemplate.setDestinationResolver(resolver);
		return jmsTemplate;
	}
}
