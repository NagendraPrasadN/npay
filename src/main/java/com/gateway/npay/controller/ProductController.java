package com.gateway.npay.controller;

import com.gateway.npay.entity.Product;
import com.gateway.npay.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalog/products")
public class ProductController {

    private final ProductService service;

    ProductController(ProductService productService){
        this.service = productService;
    }

    @PostMapping
    ResponseEntity<Product> AddProduct(@Valid @RequestBody Product product){
        Product savedProduct = service.create(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return service.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        return ResponseEntity.ok(service.update(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
