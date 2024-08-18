package com.kafka_sample.producer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

	private final ProducerService producerService;

	public ProducerController(ProducerService producerService) {
		this.producerService = producerService;
	}

	@GetMapping("/send")
	public String sendMessage(@RequestParam("topic") String topic,
	                          @RequestParam("key") String key,
	                          @RequestParam("message") String message) {
		producerService.sendMessage(topic, key, message);
		return "Message sent to Kafka topic";
	}
}