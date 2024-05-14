package com.auction.auction.controller;

import com.auction.auction.dto.ProductForBuyerProjection;
import com.auction.auction.model.Bid;
import com.auction.auction.service.AuctionService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/auctions/buyer")
@RolesAllowed("BUYER")
@RequiredArgsConstructor
public class BuyerController {

    private final AuctionService auctionService;

    @GetMapping
    public List<ProductForBuyerProjection> getAvailableProducts() {
        return auctionService.getAvailableProducts();
    }

    @Transactional
    @PostMapping("/bid/{auctionId}")
    public void placeBid(@PathVariable UUID auctionId, @Valid @RequestBody Bid bid) throws IOException {
        auctionService.placeBid(auctionId, bid);
    }
}
