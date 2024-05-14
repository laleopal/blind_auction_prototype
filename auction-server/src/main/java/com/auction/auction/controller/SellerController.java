package com.auction.auction.controller;

import com.auction.auction.model.Bid;
import com.auction.auction.model.Product;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(path = "/auctions/seller")
@RolesAllowed("SELLER")
@RequiredArgsConstructor
public class SellerController {

    @Transactional
    @PostMapping("/start")
    public Product registerProduct(@Valid @RequestBody Product product) {
        //logic for adding a new product
        return new Product();
    }

    @Transactional
    @PatchMapping("/end/{auctionId}")
    public void endAuction(@PathVariable UUID auctionId) {
        //set auction status to close
    }

    @GetMapping("/winner/{auctionId}")
    public Bid getWinner(@PathVariable UUID auctionId) {
        //add new bid
        return new Bid();
    }
}
