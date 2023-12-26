package com.aroma.shop.shop.repository;

import com.aroma.shop.shop.dto.UserDTO;
import com.aroma.shop.shop.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Products,Long> {
    @Query(value = "select * from products ORDER BY price",nativeQuery = true)
    List<Products> findCheapToDearPrice();
    @Query(value = "select * from products ORDER BY price DESC",nativeQuery = true)
    List<Products> findDearToCheapPrice();
    @Query(value = "SELECT * FROM products " +
            "WHERE (:gender IS NULL OR gender IN :gender) " +
            "AND (:color IS NULL OR color IN :color) " +
            "AND (:brands IS NULL OR category IN :brands)",
            nativeQuery = true)
    List<Products> findProductByFilter(@Param("gender") String[] gender,@Param("color") String[] color, @Param("brands")String[] brands);
}
