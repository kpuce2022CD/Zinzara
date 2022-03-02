CREATE TABLE `members` (
	`user_id` VARCHAR(30) NOT NULL COLLATE 'utf8_general_ci',
	`pw` VARCHAR(15) NOT NULL COLLATE 'utf8_general_ci',
	`phone_number` VARCHAR(13) NOT NULL COLLATE 'utf8_general_ci',
	`created` DATETIME(6) NOT NULL,
	PRIMARY KEY (`user_id`) USING BTREE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
