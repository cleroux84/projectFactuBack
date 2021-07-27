# --- !Ups
CREATE TABLE `bank` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                        `customerId` BIGINT(20) NOT NULL,
                        `name` VARCHAR(255) NOT NULL,
                        `bankCode` VARCHAR(40) NOT NULL,
                        `guichetCode` VARCHAR(40) NOT NULL,
                        `account` VARCHAR(40) NOT NULL,
                        `ribKey` DOUBLE NOT NULL,
                        `iban` VARCHAR(40) NOT NULL,

                        CONSTRAINT FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`),
                        PRIMARY KEY (`id`)
);

ALTER TABLE `customer` ADD COLUMN `bankId` VARCHAR(40);
ALTER TABLE `customer` ADD CONSTRAINT FOREIGN KEY (`bankId`) REFERENCES `bank` (`id`);

-- !Downs
DROP TABLE `bank`

