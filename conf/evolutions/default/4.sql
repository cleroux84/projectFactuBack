# --- !Ups

CREATE TABLE `benefit` (
                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                          `billId` BIGINT(20) NOT NULL,
                          `periodCovered` VARCHAR(255) NOT NULL,
                          `name` VARCHAR(255) NOT NULL,
                          `quantity` DOUBLE NOT NULL,
                          `unitPrice` DOUBLE NOT NULL,
                          `vatRate` DOUBLE NOT NULL,

                          CONSTRAINT FOREIGN KEY (`billId`) REFERENCES `bill` (`id`),
                          PRIMARY KEY (`id`)
);

-- !Downs

DROP TABLE `benefit`

