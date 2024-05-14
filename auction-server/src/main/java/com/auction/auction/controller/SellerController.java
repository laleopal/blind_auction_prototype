package com.auction.auction.controller;

import com.auction.auction.dto.WinnerBid;
import com.auction.auction.model.Product;
import com.auction.auction.service.AuctionService;
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

    private final AuctionService auctionService;

    @Transactional
    @PostMapping("/start")
    public Product registerProduct(@Valid @RequestBody Product product) {
        return auctionService.registerProduct(product);
    }

    @Transactional
    @PatchMapping("/end/{auctionId}")
    public void endAuction(@PathVariable UUID auctionId) throws IOException {
        auctionService.endAuction(auctionId);
    }

    @GetMapping("/winner/{auctionId}")
    public WinnerBid getWinner(@PathVariable UUID auctionId) throws IOException {
        return auctionService.getAuctionWinner(auctionId);
    }
}
