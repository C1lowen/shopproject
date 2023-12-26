package com.aroma.shop.shop.repository;

import com.aroma.shop.shop.model.Products;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Products> findProductByFilter(String[] gender, String[] color, String[] brands, String sorted, Double priceEnd, Double priceStart);
}
