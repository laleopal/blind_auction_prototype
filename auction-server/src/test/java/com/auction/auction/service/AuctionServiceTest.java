package com.auction.auction.service;

import com.auction.auction.dto.ProductForBuyerProjection;
import com.auction.auction.dto.WinnerBid;
import com.auction.auction.model.Bid;
import com.auction.auction.model.Product;
import com.auction.auction.model.Status;
import com.auction.auction.repository.BidRepository;
import com.auction.auction.repository.ProductRepository;
import com.auction.auction.testdata.TestData;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuctionServiceTest {

    private final TestData testData = new TestData();

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BidRepository bidRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuctionService auctionService;

    @Test
    @Description("test adding new product")
    void testRegisterProduct() {
        Product product = new Product();
        UUID id = UUID.randomUUID();
        product.setId(id);
        product.setStatus(Status.OPEN);
        product.setMinimalBid(100L);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product registeredProduct = auctionService.registerProduct(product);

        assertNotNull(registeredProduct);
        assertEquals(id, registeredProduct.getId());
        assertNull(registeredProduct.getStatus());
        assertNull(registeredProduct.getMinimalBid());
    }

    @Test
    @Description("test successful closing of an auction")
    void testEndAuction()  {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setStatus(Status.OPEN);
        product.setMinimalBid(100L);

        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));

        auctionService.endAuction(product.getId());

        assertEquals(Status.CLOSED, product.getStatus());
    }

    @Test
    @Description("test successfully getting auction winner")
    void testGetAuctionWinner_Found()  {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setStatus(Status.CLOSED);
        product.setMinimalBid(100L);

        Bid winnerBid = new Bid();
        winnerBid.setId(UUID.randomUUID());
        winnerBid.setUserToken("testUserToken");
        winnerBid.setBid(200L);

        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));
        when(bidRepository.getWinner(any(UUID.class))).thenReturn(Optional.of(winnerBid));
        when(jwtService.extractSubject(any())).thenReturn("testUser");

        WinnerBid actualWinner = auctionService.getAuctionWinner(product.getId());

        assertNotNull(actualWinner);
        assertEquals(winnerBid.getId(), actualWinner.getId());
        assertEquals(winnerBid.getBid(), actualWinner.getBid());
        assertEquals("testUser", actualWinner.getUsername());
    }

    @Test
    @Description("test unsuccessfully getting auction winner")
    void testGetAuctionWinner_NotFound()  {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setStatus(Status.CLOSED);
        product.setMinimalBid(100L);

        Bid winnerBid = new Bid();
        winnerBid.setId(UUID.randomUUID());
        winnerBid.setUserToken("testUserToken");
        winnerBid.setBid(200L);

        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));
        when(bidRepository.getWinner(any(UUID.class))).thenThrow(new NoSuchElementException("Repository exception"));

        assertThrows(NoSuchElementException.class, () -> auctionService.getAuctionWinner(product.getId()));
        verify(productRepository, times(1)).findById(product.getId());
        verify(bidRepository, times(1)).getWinner(product.getId());
        verify(jwtService, times(2)).extractSubject(any());
    }



    @Test
    @Description("test getAvailableProducts with objects found")
    public void testGetAvailableProducts_Found() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        String name1 = "Product 1";
        String name2 = "Product 2";
        String status = "OPEN";

        ProductForBuyerProjection product1 = testData.getProductProjection(id1, name1, status);
        ProductForBuyerProjection product2 = testData.getProductProjection(id2, name2, status);

        List<ProductForBuyerProjection> dummyProducts = Arrays.asList(product1, product2);
        when(productRepository.findOpenAuctions()).thenReturn(dummyProducts);

        List<ProductForBuyerProjection> availableProducts = auctionService.getAvailableProducts();

        assertThat(availableProducts).isNotNull();
        assertThat(availableProducts.size()).isEqualTo(2);
        assertThat(availableProducts).containsExactly(product1, product2);
    }

    @Test
    @Description("test getAvailableProducts with objects not found")
    public void testGetAvailableProducts_EmptyList() {
        when(productRepository.findOpenAuctions()).thenReturn(Collections.emptyList());

        List<ProductForBuyerProjection> availableProducts = auctionService.getAvailableProducts();

        assertThat(availableProducts).isNotNull();
        assertThat(availableProducts).isEmpty();
    }

    @Test
    @Description("successfully saving new bid")
    void testPlaceBid_Successful() {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setStatus(Status.OPEN);
        product.setMinimalBid(50L);

        Bid bid = new Bid();
        bid.setBid(60L);

        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));

        assertDoesNotThrow(() -> auctionService.placeBid(product.getId(), bid));
    }

    @Test
    @Description("successfully saving new bid")
    void testPlaceBid_NotFound()  {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setStatus(Status.OPEN);
        product.setMinimalBid(50L);

        Bid bid = new Bid();
        bid.setBid(60L);

        when(productRepository.findById(any(UUID.class))).thenThrow(new NoSuchElementException("Product not found"));

        assertThrows(NoSuchElementException.class, () -> auctionService.placeBid(product.getId(), bid));
    }
}
