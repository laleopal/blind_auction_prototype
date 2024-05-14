--Create table to save Product
CREATE TABLE IF NOT EXISTS product (
    id uuid PRIMARY KEY UNIQUE,
    name varchar(255) NULL,
    minimal_bid int8 NOT NULL,
    seller text NULL,
    status varchar(255) NULL
);

--Create table to save Bid
CREATE TABLE IF NOT EXISTS bid (
    id uuid PRIMARY KEY UNIQUE,
    user_token text NULL,
    bid int8 NOT NULL,
    product_id uuid NOT NULL,
    CONSTRAINT bid__product FOREIGN KEY (product_id) REFERENCES product(id)
);