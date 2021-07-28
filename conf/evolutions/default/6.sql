# --- !Ups

CREATE TABLE `user` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                        `civility` VARCHAR(255) NOT NULL,
                        `firstName` VARCHAR(255) NOT NULL,
                        `lastName` VARCHAR(255) NOT NULL,
                        `email` VARCHAR(255) NOT NULL,
                        `phone` VARCHAR(255) NOT NULL,
                        `address` VARCHAR(255) NOT NULL,
                        `city` VARCHAR(255) NOT NULL,
                        `zipCode` VARCHAR(255) NOT NULL,
                        `siret` VARCHAR(255) NOT NULL,
                        `bankId` BIGINT(20) NOT NULL,
                        PRIMARY KEY (`id`)
);

-- !Downs

DROP TABLE `user`