package com.auction.auction.service;

import com.auction.auction.dto.ProductForBuyerProjection;
import com.auction.auction.dto.WinnerBid;
import com.auction.auction.model.Bid;
import com.auction.auction.model.Product;
import com.auction.auction.model.Status;
import com.auction.auction.repository.BidRepository;
import com.auction.auction.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final ProductRepository productRepository;
    private final BidRepository bidRepository;
    private final JwtService jwtService;

    public Product registerProduct(Product product) {
        product.setSeller(getAuthenticatedUser());
        product.setStatus(Status.OPEN);
        productRepository.save(product);
        return new Product(product.getId());
    }

    public void endAuction(UUID id)  {
        Product product = getProduct(id);
        if (isSameSeller(product) && !isAuctionClosed(product)) {
            product.setStatus(Status.CLOSED);
            productRepository.save(product);
        } else {
            throw new RuntimeException("not authorized or incorrect auction status");
        }
    }

    public WinnerBid getAuctionWinner(UUID id)  {
        Product product = getProduct(id);
        if (isSameSeller(product) && isAuctionClosed(product)) {
            Bid winner = bidRepository.getWinner(product.getId()).orElseThrow();
            return new WinnerBid(
                    winner.getId(),
                    jwtService.extractSubject(winner.getUserToken()),
                    winner.getBid()
            );
        } else {
            throw new RuntimeException("not authorized or incorrect auction status");
        }
    }

    public List<ProductForBuyerProjection> getAvailableProducts() {
        return productRepository.findOpenAuctions();
    }

    public void placeBid(UUID id, Bid bid) {
        Product product = getProduct(id);

        if (!isAuctionClosed(product) && !isInvalidBid(bid, product)) {
            bid.setProduct(product);
            bid.setUserToken(getAuthenticatedUser());
            bidRepository.save(bid);
        } else {
            throw new RuntimeException("auction closed or invalid bid");
        }
    }

    private Product getProduct(UUID id) {
        return productRepository.findById(id).orElseThrow();
    }

    private boolean isSameSeller (Product product) {
        return Objects.equals(extractUsername(product.getSeller()), extractUsername(getAuthenticatedUser()));
    }

    private boolean isAuctionClosed(Product product) {
        return Status.CLOSED.equals(product.getStatus());
    }

    private boolean isInvalidBid(Bid bid, Product product) {
        return bid.getBid() < product.getMinimalBid();
    }

    private String getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            return ((Jwt) authentication.getPrincipal()).getTokenValue();
        }

        return null;
    }

    private String extractUsername(String token) {
        return jwtService.extractSubject(token);
    }
}
