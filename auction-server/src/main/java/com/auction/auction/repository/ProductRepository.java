package com.auction.auction.repository;

import com.auction.auction.dto.ProductForBuyerProjection;
import com.auction.auction.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT p FROM Product p WHERE p.status = 'OPEN'")
    List<ProductForBuyerProjection> findOpenAuctions();
}
