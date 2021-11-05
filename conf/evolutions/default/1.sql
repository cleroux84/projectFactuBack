# --- !Ups

CREATE TABLE `customer` (
                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                          `civility` VARCHAR(255) NOT NULL,
                          `firstName` VARCHAR(255) NOT NULL,
                          `lastName` VARCHAR(255) NOT NULL,
                          `email` VARCHAR(255) NOT NULL UNIQUE,
                          `phone` VARCHAR(255) NOT NULL,
                          `phone2` VARCHAR(255),
                          `address` VARCHAR(255) NOT NULL,
                          `city` VARCHAR(255) NOT NULL,
                          `zipCode` VARCHAR(255) NOT NULL,
                          `company` VARCHAR(255),
                          PRIMARY KEY (`id`)
);

# --- !Downs

DROP TABLE `customer` if exists;
