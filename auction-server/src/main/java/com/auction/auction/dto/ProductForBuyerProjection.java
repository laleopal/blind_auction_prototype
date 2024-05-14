package com.auction.auction.dto;

import java.util.UUID;

public interface ProductForBuyerProjection {
    UUID getId();
    String getName();
    String getStatus();
}
