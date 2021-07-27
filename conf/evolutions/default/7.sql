# --- !Ups

ALTER TABLE `customer` ADD COLUMN `bankId` BIGINTÒ;
ALTER TABLE `customer` ADD CONSTRAINT FOREIGN KEY (`bankId`) REFERENCES `bank` (`id`);

-- !Downs