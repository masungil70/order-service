CREATE TABLE `orders` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`book_isbn` VARCHAR(50) NULL DEFAULT '0' ,
	`book_name` VARCHAR(50) NULL DEFAULT '0' ,
	`book_price` DOUBLE NULL DEFAULT '0',
	`quantity` INT(11) NULL DEFAULT '0',
	`status` VARCHAR(50) NULL DEFAULT '0' ,
	`created_date` TIMESTAMP NULL DEFAULT NULL,
	`last_modified_date` TIMESTAMP NULL DEFAULT NULL,
	`version` INT(11) NULL DEFAULT '0',
	PRIMARY KEY (`id`) USING BTREE
)
;
