package com.example.rabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.topic.name}")
    private String exchange;

    @Value("${rabbit.routing.Key}")
    private String routingkey;

    @Value("${rabbitmq.jsonqueue.name}")
    private String jsonqueue;


    @Value("${rabbit.jsonrouting.Key}")
    private String jsonroutingkey;

    // spring bean for rabbitmq queue
    @Bean
    public Queue queue() {
        return new Queue(queue);
    }

    // spring topic exchange
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    // configure rmq to use json and create pojo class to serialize or deserialize

    @Bean
    public Queue jsonqueue() {
        return new Queue(queue);
    }


    // spring binding queue and exchange using routing key

    @Bean
    public Binding bind() {
        return BindingBuilder.bind(queue()).to(exchange()).with(routingkey);
    }

    @Bean
    public Binding jsonbind() {
        return BindingBuilder.bind(jsonqueue()).to(exchange()).with(jsonroutingkey);
    }


    // connectionFactory, RabbitTempate, RabbitAdmin all these are autoconfigured no
    // need to setup explicitly

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;

    }
}
