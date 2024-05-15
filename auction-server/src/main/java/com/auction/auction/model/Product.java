package com.auction.auction.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table
public class Product {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String name;

    @NotNull
    private Long minimalBid;

    private String seller;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL
    )
    @OrderBy("bid")
    private List<Bid> bids;

    public Product(UUID id) {
        this.id = id;
    }

    public Product(String name, UUID id, Long minimalBid, Status status) {
        this.name = name;
        this.id = id;
        this.minimalBid = minimalBid;
        this.status = status;
    }
}
