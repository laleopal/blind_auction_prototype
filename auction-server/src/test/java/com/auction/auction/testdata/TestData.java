package com.auction.auction.testdata;

import com.auction.auction.dto.ProductForBuyerProjection;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TestData {
    public ProductForBuyerProjection getProductProjection(UUID id, String name, String status) {
        return new ProductForBuyerProjection() {
            @Override
            public UUID getId() {
                return id;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getStatus() {
                return status;
            }
        };
    }
}
