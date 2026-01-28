package com.gateway.npay.service;

import com.gateway.npay.entity.Product;
import com.gateway.npay.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class ProductService {


    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @CacheEvict(value = "products", key = "'allProducts'")
    public Product create(Product product) {
      return  repository.save(product);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "'allProducts'")
    public List<Product> getAll(){
        System.out.println("Fetching from DB...");
        return repository.findAll();
    }

    @Transactional
    @CacheEvict(value = "products", key = "'allProducts'")
    public Product update(Long id, Product updatedProduct) {
        return repository.findById(id).map(product -> {
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setStockQuantity(updatedProduct.getStockQuantity());
            return repository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Transactional
    @CacheEvict(value = "products", key = "'allProducts'")
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        repository.deleteById(id);
    }

}
