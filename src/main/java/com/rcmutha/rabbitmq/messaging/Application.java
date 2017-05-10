package com.rcmutha.rabbitmq.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	final static String queueName = "spring-boot";
	final static String queueName1 = "roshanTest";
	@Bean(name="spring-boot")
	Queue queue() {
		return new Queue(queueName, false);
	}
	
	@Bean(name="roshanTest")
	Queue queue1() {
		return new Queue(queueName1, false);
	}
	
	
	@Bean(name="spring-boot-topic")
	TopicExchange exchange() {
		return new TopicExchange("spring-boot-exchange");
	}
	
	@Bean(name="roshanTest-topic")
	TopicExchange exchange1() {
		return new TopicExchange("roshanTest-exchange");
	}
	
	@Bean
    Binding binding(@Qualifier("spring-boot")Queue queue, @Qualifier("spring-boot-topic")TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }
	
	@Bean
    Binding binding1(@Qualifier("roshanTest")Queue queue, @Qualifier("roshanTest-topic")TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName1);
    }
	
	@Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName,queueName1);
        //container.setQueueNames(queueName1);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Application.class, args);
    }
}
