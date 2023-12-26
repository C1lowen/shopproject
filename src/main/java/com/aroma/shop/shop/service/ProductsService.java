package com.aroma.shop.shop.service;

import com.aroma.shop.shop.dto.ConditionProducts;
import com.aroma.shop.shop.model.Products;
import com.aroma.shop.shop.repository.ProductRepositoryCustomImpl;
import com.aroma.shop.shop.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ProductsService {
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private ProductRepositoryCustomImpl productRepositoryCustom;
    public void saveProducts(Products products) {
        productsRepository.save(products);
    }
    public List<Products> findAllProducts(){
        return productsRepository.findAll();
    }

    public List<Products> findCheapToDearPrice(){
        return productsRepository.findCheapToDearPrice();
    }

    public List<Products> findDearToCheapPrice(){
       return productsRepository.findDearToCheapPrice();
    }

    public List<Products> findByCondition(ConditionProducts filter){
        return productRepositoryCustom.findProductByFilter(filter.getGender(), filter.getColor(), filter.getBrands(), filter.getSorted(), filter.getPriceEnd(),  filter.getPriceStart());
    }


    public Page<Products> findPaginated(Pageable pageable, ConditionProducts filter) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Products> list;
        List<Products> posts = findByCondition(filter);
        if (posts.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, posts.size());
            list = posts.subList(startItem, toIndex);
        }

        Page<Products> bookPage
                = new PageImpl<Products>(list, PageRequest.of(currentPage, pageSize), posts.size());

        return bookPage;
    }


    public void listNumber(Integer firstNum,Integer lastNum, Model model){
        List<Integer> pageNumbers = IntStream.rangeClosed(firstNum, lastNum)
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("pageNumbers", pageNumbers);
    }
    public Optional<Products> findProductById(Long id){
        return productsRepository.findById(id);
    }
}
