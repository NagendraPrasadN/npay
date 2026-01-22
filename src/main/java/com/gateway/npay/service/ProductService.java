package com.gateway.npay.service;

import com.gateway.npay.entity.Product;
import com.gateway.npay.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {


    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product create(Product product) {
      return  repository.save(product);
    }

    public List<Product> getAll(){
        return repository.findAll();
    }

    public Product update(Long id, Product updatedProduct) {
        return repository.findById(id).map(product -> {
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setStockQuantity(updatedProduct.getStockQuantity());
            return repository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        repository.deleteById(id);
    }

}
