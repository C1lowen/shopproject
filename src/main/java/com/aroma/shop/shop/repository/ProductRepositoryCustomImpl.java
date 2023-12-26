package com.aroma.shop.shop.repository;

import com.aroma.shop.shop.model.Products;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final EntityManager entityManager;

    public ProductRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Products> findProductByFilter(String[] gender, String[] color, String[] brands, String sorted, Double priceEnd, Double priceStart) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Products> query = cb.createQuery(Products.class);
        Root<Products> root = query.from(Products.class);

        List<Predicate> predicates = new ArrayList<>();

        if (gender != null && gender.length > 0) {
            predicates.add(root.get("gender").in((Object[]) gender));
        }

        if (color != null && color.length > 0) {
            predicates.add(root.get("color").in((Object[]) color));
        }

        if (brands != null && brands.length > 0) {
            predicates.add(root.get("category").in((Object[]) brands));
        }

        if (priceStart != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), priceStart));
        }

        if (priceEnd != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), priceEnd));
        }

        query.where(predicates.toArray(new Predicate[0]));

        if ("cheap".equals(sorted)) {
            query.orderBy(cb.asc(root.get("price")));
        } else if ("expensive".equals(sorted)) {
            query.orderBy(cb.desc(root.get("price")));
        }

        return entityManager.createQuery(query).getResultList();
    }
}
