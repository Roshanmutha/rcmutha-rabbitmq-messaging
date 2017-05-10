package com.rcmutha.rabbitmq.messaging;

import java.util.concurrent.CountDownLatch;

import org.springframework.stereotype.Component;

@Component
public class Receiver {
	private CountDownLatch latch = new CountDownLatch(45);

	public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        latch.countDown();
        System.out.println("Received end");
    }
	
	public CountDownLatch getLatch() {
        return latch;
    }
	
}
