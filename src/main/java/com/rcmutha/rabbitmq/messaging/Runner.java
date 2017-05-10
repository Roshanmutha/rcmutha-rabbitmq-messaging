package com.rcmutha.rabbitmq.messaging;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;
    private final ConfigurableApplicationContext context;

    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate,
            ConfigurableApplicationContext context) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
        this.context = context;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Sending message...");
        Thread t1=new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < 20; i++) {
					rabbitTemplate.convertAndSend(Application.queueName, "t1 from RabbitMQ!"+i);
					rabbitTemplate.convertAndSend(Application.queueName1, "t1 from RabbitMQ one!"+i);
				}
			}
		});
        t1.start();
        Thread t2=new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < 20; i++) {
					rabbitTemplate.convertAndSend(Application.queueName, "t2 from RabbitMQ!"+i);
					rabbitTemplate.convertAndSend(Application.queueName1, "t2 from RabbitMQ one!"+i);
				}
			}
		});
        t2.start();
        receiver.getLatch().await(1, TimeUnit.DAYS);
		context.close();
    }

}
