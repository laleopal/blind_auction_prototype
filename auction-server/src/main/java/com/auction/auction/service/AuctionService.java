package com.auction.auction.service;

import com.auction.auction.dto.ProductForBuyerProjection;
import com.auction.auction.dto.WinnerBid;
import com.auction.auction.model.Bid;
import com.auction.auction.model.Product;
import com.auction.auction.model.Status;
import com.auction.auction.repository.BidRepository;
import com.auction.auction.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final ProductRepository productRepository;
    private final BidRepository bidRepository;

    public Product registerProduct(Product product) {
        product.setSeller("seller");
        product.setStatus(Status.OPEN);
        productRepository.save(product);
        return new Product(product.getId());
    }

    public void endAuction(UUID id) throws IOException {
        Product product = getProduct(id);
        if (isSameSeller(product) && !isAuctionClosed(product)) {
            product.setStatus(Status.CLOSED);
            productRepository.save(product);
        } else {
            throw new IOException("not authorized or incorrect auction status");
        }
    }

    public WinnerBid getAuctionWinner(UUID id) throws IOException {
        Product product = getProduct(id);
        if (isSameSeller(product) && isAuctionClosed(product)) {
            Bid winner = bidRepository.getWinner(product.getId()).orElseThrow();
            return new WinnerBid(
                    winner.getId(),
                    "winner",
                    winner.getBid()
            );
        } else {
            throw new IOException("not authorized or incorrect auction status");
        }
    }

    public List<ProductForBuyerProjection> getAvailableProducts() {
        return productRepository.findOpenAuctions();
    }

    public void placeBid(UUID id, Bid bid) throws IOException {
        Product product = getProduct(id);

        if (!isAuctionClosed(product) && !isInvalidBid(bid, product)) {
            bid.setProduct(product);
            bid.setUserToken("user");
            bidRepository.save(bid);
        } else {
            throw new IOException("auction closed or invalid bid");
        }
    }

    private Product getProduct(UUID id) {
        return productRepository.findById(id).orElseThrow();
    }

    private boolean isSameSeller (Product product) {
        //compare sellers
        return true;
    }

    private boolean isAuctionClosed(Product product) {
        return Status.CLOSED.equals(product.getStatus());
    }

    private boolean isInvalidBid(Bid bid, Product product) {
        return bid.getBid() < product.getMinimalBid();
    }
}
