package com.ye13sh.productservice.Configuration;

import com.ye13sh.productservice.Event.ProductEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic createTopic(){
        Map<String, String> configs = new HashMap<>();
        configs.put("min.insync.replicas", "2");// out 3 replicas, 2 are inSync

        return TopicBuilder.name("product-event-topic")
                .partitions(3)
                .replicas(3) //number of brokers/server directly prop to number of replicas
                .configs(configs)
                .build();
    }
}
