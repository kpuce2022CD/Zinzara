CREATE TABLE `members` (
	`user_id` VARCHAR(30) NOT NULL COLLATE 'utf8_general_ci',
	`pw` VARCHAR(15) NOT NULL COLLATE 'utf8_general_ci',
	`phone_number` VARCHAR(13) NOT NULL COLLATE 'utf8_general_ci',
	`created` DATETIME(6) NOT NULL,
	PRIMARY KEY (`user_id`) USING BTREE
);


CREATE TABLE `devices` (
	`device_name` VARCHAR(20) NOT NULL COLLATE 'utf8_general_ci',
	`device_command` VARCHAR(10) NOT NULL COLLATE 'utf8_general_ci',
	`device_command_time` DATETIME(6) NOT NULL,
	`idx` INT(11) NOT NULL AUTO_INCREMENT,
	`user_id` VARCHAR(30) NOT NULL COLLATE 'utf8_general_ci',
	PRIMARY KEY (`idx`) USING BTREE,
	INDEX `devices_user_id_9a5cca49` (`user_id`) USING BTREE,
	CONSTRAINT `devices_user_id_9a5cca49_fk_members_user_id` FOREIGN KEY (`user_id`) REFERENCES `zinzara`.`members` (`user_id`) ON UPDATE RESTRICT ON DELETE RESTRICT
);


CREATE TABLE `physical_rehabilitation` (
	`physical_score` INT(11) NOT NULL,
	`rehabilitation_time` DATETIME(6) NOT NULL,
	`idx` INT(11) NOT NULL AUTO_INCREMENT,
	`user_id` VARCHAR(30) NOT NULL COLLATE 'utf8_general_ci',
	PRIMARY KEY (`idx`) USING BTREE,
	INDEX `physical_rehabilitation_user_id_dec6d569` (`user_id`) USING BTREE,
	CONSTRAINT `physical_rehabilitation_user_id_dec6d569_fk_members_user_id` FOREIGN KEY (`user_id`) REFERENCES `zinzara`.`members` (`user_id`) ON UPDATE RESTRICT ON DELETE RESTRICT
);
