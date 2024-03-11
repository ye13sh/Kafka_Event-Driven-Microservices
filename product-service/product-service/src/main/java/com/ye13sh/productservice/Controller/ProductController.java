package com.ye13sh.productservice.Controller;

import com.ye13sh.productservice.Model.Product;
import com.ye13sh.productservice.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductService service;
    @PostMapping("/add")
    public ResponseEntity<String> createProduct(@RequestBody Product product){
        String productID = service.createProduct(product);
        return ResponseEntity.ok("Product added with ID:" +" "+productID);
    }
}
