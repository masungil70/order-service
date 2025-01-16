CREATE TABLE `orders` (
	id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    book_isbn   varchar(255) not null,
    book_name  varchar(255) not null,
	book_price DOUBLE NOT NULL DEFAULT '0',
	quantity INT(11) NOT NULL DEFAULT '0',
	status varchar(255) not null,
	created_date  timestamp not null,
	last_modified_date timestamp not null,
	version INT(11) NULL not 
)
;
