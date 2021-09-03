create table `user` (
id bigint not null auto_increment,
first_name varchar(255),
second_name varchar(255),
email varchar(255) unique,
phone_number varchar(255) unique,
password varchar(255),
profile_photo varchar(255),
location varchar(255),
registered_at datetime(6),
last_login datetime(6),
status varchar(255),
role varchar(255),
primary key (id)
)
engine=InnoDB;

create table `ad` (
id bigint not null auto_increment,
author_id bigint not null,
title varchar(255),
description varchar(255),
category_id bigint not null,
price decimal(19,2),
release_year date,
created_at datetime(6),
status varchar(255),
primary key (id),
constraint FK_author_ad FOREIGN KEY (author_id)  REFERENCES user (id)
)
engine=InnoDB;

create TABLE `ad_photo` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`ad_id` bigint NOT NULL,
	`photo` VARCHAR(255),
	PRIMARY KEY (`id`),
	CONSTRAINT `FK_photos_ad` FOREIGN KEY (`ad_id`) REFERENCES ad (id)
)
ENGINE=InnoDB;

create TABLE `category` (
	`id` BIGINT(19) NOT NULL AUTO_INCREMENT,
	`category_name` VARCHAR(255) NOT NULL,
	`parent_id` BIGINT(19) NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `FK_parent_children` FOREIGN KEY (`parent_id`) REFERENCES category (`id`)
)
ENGINE=InnoDB
;

