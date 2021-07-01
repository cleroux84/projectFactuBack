# --- !Ups

CREATE TABLE `bill` (
                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                          `customerId` BIGINT(20) NOT NULL,
                          `created` DATETIME NOT NULL DEFAULT NOW(),
                          `periodCovered` VARCHAR(255) NOT NULL,
                          `billNumber` VARCHAR(40) NOT NULL UNIQUE ,
                          `benefit` VARCHAR(255) NOT NULL,
                          `quantity` DOUBLE NOT NULL,
                          `unitPrice` DOUBLE NOT NULL,
                          `vatRate` DOUBLE NOT NULL,

                          CONSTRAINT FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`),
                          PRIMARY KEY (`id`)
);

-- !Downs

DROP TABLE `bill`;

