package com.auction.auction.service;

import com.auction.auction.dto.ProductForBuyerProjection;
import com.auction.auction.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final ProductRepository productRepository;

    public List<ProductForBuyerProjection> getAvailableProducts() {
        return productRepository.findOpenAuctions();
    }
}
