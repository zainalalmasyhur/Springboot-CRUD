package com.zainal.test.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zainal.test.models.Product;

public interface ProductRepository  extends JpaRepository<Product, Integer>{

}
