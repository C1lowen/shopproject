package com.aroma.shop.shop.controller;

import com.aroma.shop.shop.model.Products;
import com.aroma.shop.shop.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class ProductController {
    @Autowired
    private ProductsService productsService;

    @GetMapping("shop/product/{id}")
    public String productPage(@PathVariable Long id, Model model){
        Optional<Products> product = productsService.findProductById(id);
        if(product.isEmpty()) return null;
        model.addAttribute("product", product.get());
        return "single-product";
    }
}
