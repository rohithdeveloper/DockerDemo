package com.example.rabbit.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.rabbit.model.RabbitMQMessage;

@Repository
public interface RmqRepository extends JpaRepository<RabbitMQMessage, Integer> {
}

