# --- !Ups

ALTER TABLE `bank` ADD COLUMN `userId` BIGINT;
ALTER TABLE `bank` ADD CONSTRAINT FOREIGN KEY (`userId`) REFERENCES `user` (`id`);


-- !Downs
