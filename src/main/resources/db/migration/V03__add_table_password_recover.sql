CREATE TABLE `products`.`tb_password_recover` (
      `id` BIGINT NOT NULL AUTO_INCREMENT,
      `token` VARCHAR(70) NOT NULL,
      `email` VARCHAR(200) NOT NULL,
      `expiration` DATETIME NOT NULL,
      PRIMARY KEY (`id`)
);