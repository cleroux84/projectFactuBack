# --- !Ups

ALTER TABLE `benefit` DROP COLUMN `periodCovered`;

ALTER TABLE `bill` DROP COLUMN `benefit`;
ALTER TABLE `bill` DROP COLUMN `quantity`;
ALTER TABLE `bill` DROP COLUMN `unitPrice`;
ALTER TABLE `bill` DROP COLUMN `vatRate`;

-- !Downs


