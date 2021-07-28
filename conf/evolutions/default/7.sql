# --- !Ups

CREATE TABLE `bank` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                        `name` VARCHAR(255) NOT NULL,
                        `bankCode` VARCHAR(40) NOT NULL,
                        `guichetCode` VARCHAR(40) NOT NULL,
                        `account` VARCHAR(40) NOT NULL,
                        `ribKey` DOUBLE NOT NULL,
                        `iban` VARCHAR(40) NOT NULL,
                        PRIMARY KEY (`id`)
);

-- !Downs

DROP TABLE `bank`