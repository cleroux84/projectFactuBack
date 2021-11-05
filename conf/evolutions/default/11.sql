# --- !Ups

ALTER TABLE `bill` ADD COLUMN `paid` BOOLEAN DEFAULT FALSE;
ALTER TABLE `bill` ADD COLUMN `paymentDate` DATETIME;


-- !Downs
