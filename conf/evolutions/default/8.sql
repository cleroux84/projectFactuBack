# --- !Ups

ALTER TABLE `bill` ADD COLUMN `userId` BIGINT;
ALTER TABLE `bill` ADD CONSTRAINT FOREIGN KEY (`userId`) REFERENCES `user` (`id`);
ALTER TABLE `user` ADD CONSTRAINT FOREIGN KEY (`bankId`) REFERENCES `bank` (`id`);

-- !Downs
