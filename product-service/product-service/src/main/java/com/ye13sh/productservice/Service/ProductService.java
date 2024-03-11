package com.ye13sh.productservice.Service;

//import com.ye13sh.productservice.Event.ProductEvent;
import com.ye13sh.ProductEvent;
import com.ye13sh.productservice.Model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductService {
    @Autowired
    KafkaTemplate<String, ProductEvent> kafkaTemplate;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public String createProduct(Product product){ // We need to call repo to save data in DB
        String productID = UUID.randomUUID().toString();

        ProductEvent productEvent = new ProductEvent(); // To create event

        productEvent.setProductID(productID);
        productEvent.setProductName(product.getProductName());
        productEvent.setQuantity(product.getQuantity());
        productEvent.setPrice(product.getPrice());



        //kafkaTemplate.send("product-event-topic",productID,productEvent); //Synchronous

        CompletableFuture<SendResult<String,ProductEvent>> future = //String in CompletableFuture<SendResult<String,ProductEvent>> is productID (key,value)
                kafkaTemplate.send("product-event-topic",productID,productEvent).completable();//productID is key and productEvent is value  //Asynchronous

        future.whenComplete((result,exception) -> { // To get message in log after complete
            if(exception!=null){
                logger.error("Failed to send message" + exception.getMessage());
            }else {
                logger.info("Message sent successfully" + result.getRecordMetadata().partition());//.partition() shows in which partition the message has stored
            }
        });
        logger.info("Returning product id");

        //future.join(); //this will block the thread and wait till to get the result and this will become synchronous again
        return productID;
    }
}
