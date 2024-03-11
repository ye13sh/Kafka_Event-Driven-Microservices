package com.ye13sh.Handler;

import com.ye13sh.Exception.NonRetryableException;
import com.ye13sh.Exception.RetryableException;
import com.ye13sh.ProductEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@KafkaListener(topics = "product-event-topic")
public class Product_Created_Event_Handler {
    @Autowired
    RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @KafkaHandler
    public void handle(ProductEvent event){
        logger.info("Received a new event"+" "+event.getProductName());

        String url = "http://localhost:8081/response/500";
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,null,String.class);

            if(response.getStatusCode().value() == HttpStatus.OK.value()){
                logger.info("Received response from a remote service"+response.getBody());
            }
        }catch (ResourceAccessException e){
            logger.error(e.getMessage());
            throw new RetryableException(e);
        }catch (HttpServerErrorException e){
            throw new NonRetryableException(e);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new NonRetryableException(e);
        }

    }
}
