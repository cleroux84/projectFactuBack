# --- !Ups

ALTER TABLE `user` ADD COLUMN `role` INT;
ALTER TABLE `user` ADD COLUMN `authId` VARCHAR(255) ;

-- !Downs
