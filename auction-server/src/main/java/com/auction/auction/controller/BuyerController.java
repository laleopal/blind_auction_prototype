package com.auction.auction.controller;

import com.auction.auction.model.Bid;
import com.auction.auction.model.Product;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/auctions/buyer")
@RolesAllowed("BUYER")
@RequiredArgsConstructor
public class BuyerController {
    @GetMapping
    public List<Product> getAvailableProducts() {
        //get all open auctions
        return new ArrayList<>();
    }

    @Transactional
    @PostMapping("/bid/{auctionId}")
    public void placeBid(@PathVariable UUID auctionId, @Valid @RequestBody Bid bid) {
       //add a bid
    }
}
