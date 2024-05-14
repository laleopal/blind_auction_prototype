package com.auction.auction.repository;

import com.auction.auction.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BidRepository extends JpaRepository<Bid, UUID> {

    @Query("SELECT b FROM Bid b WHERE b.product.id = ?1 ORDER BY b.bid DESC LIMIT 1")
    Optional<Bid> getWinner(UUID productId);
}
