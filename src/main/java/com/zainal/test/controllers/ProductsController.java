package com.zainal.test.controllers;

import java.util.List;
import java.util.Date;

import org.hibernate.internal.util.collections.Stack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zainal.test.models.Product;
import com.zainal.test.models.ProductDto;
import com.zainal.test.services.ProductRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository repo;
    
    @GetMapping({"","/"})
    public String showProductList(Model model){
        List<Product> products = repo.findAll();
        model.addAttribute("products", products);
        return "products/index" ;
    }

    @GetMapping("/create")
    public String showCreatePage(Model model){
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        return "products/CreateProduct";
    }

    @PostMapping("/create")
    public String createProduct(
        @Valid @ModelAttribute ProductDto productDto,
        BindingResult result
        ) {
            if (result.hasErrors()) {

                return "products/CreateProduct";
            }

            Date createdAt = new Date();

            Product product = new Product();
            product.setName(productDto.getName());
            product.setType(productDto.getType());
            product.setPrice(productDto.getPrice());
            product.setQty(productDto.getQty());
            product.setCreatedAt(createdAt);

            repo.save(product);

            return "redirect:/products";
        }

    @GetMapping("/edit")
    public String showEditPage(
        Model model,
        @RequestParam int id
        ) {
            try {
                Product product = repo.findById(id).get();
                model.addAttribute("product", product);

                ProductDto productDto = new ProductDto();
                productDto.setName(product.getName());
                productDto.setType(product.getType());
                productDto.setPrice(product.getPrice());
                productDto.setQty(product.getQty());

                model.addAttribute("productDto", productDto);

            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
                return "redirect:/products";
            }
            
            return "products/EditProduct";
        }

    @PostMapping("/edit")
    public String UpdateProduct(
        Model model,
        @RequestParam int id,
        @Valid @ModelAttribute ProductDto productDto,
        BindingResult result
    ) {

        try {
            Product product = repo.findById(id).get();
            model.addAttribute("product", product);

            if (result.hasErrors()) {
                return "products/EditProduct";
            }

            product.setName(productDto.getName());
            product.setType(productDto.getType());
            product.setPrice(productDto.getPrice());
            product.setQty(productDto.getQty());

            repo.save(product);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("delete")
    public String deleteProduct (
        @RequestParam int id
    ) {
        try {
            Product product = repo.findById(id).get();

            repo.delete(product);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/products";
    }
    
}
