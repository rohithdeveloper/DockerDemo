package com.example.rabbit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbit.model.RabbitMQMessage;
import com.example.rabbit.producer.RabbitMQProducer;
import com.example.rabbit.repository.RmqRepository;
@RestController
@RequestMapping("/api")
public class MessageController {

	@Autowired
	private RabbitMQProducer rmqProducer;

	@Autowired
	private RmqRepository rmqRepo;

//	@GetMapping("/publish")
//	public ResponseEntity<String> sendMessage(@RequestParam("message") String message){
//		rmqProducer.sendMessage(message);
//		return ResponseEntity.ok("Message sent to rmq successfully");
//	}

	@PostMapping("/publish")
	public ResponseEntity<String> sendMessage(@RequestBody RabbitMQMessage request) {
		rmqProducer.sendMessage(request.getMessage());
		rmqRepo.save(request);
		return ResponseEntity.ok("Message sent to RMQ successfully");
	}
}
