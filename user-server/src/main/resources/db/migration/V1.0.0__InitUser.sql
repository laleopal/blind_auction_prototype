--Create table to save Product
CREATE TABLE IF NOT EXISTS auction_user (
    id uuid DEFAULT RANDOM_UUID() PRIMARY KEY UNIQUE,
    username varchar(255) NULL,
    password varchar(255) NULL,
    role varchar(255) NULL
);