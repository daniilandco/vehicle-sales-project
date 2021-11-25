create TABLE `Token`
(
    id      BIGINT       NOT NULL auto_increment,
    user_id BIGINT       NOT NULL,
    token   VARCHAR(255) NOT NULL,
    CONSTRAINT `fk_token_user` FOREIGN KEY (`user_id`) REFERENCES `User` (id),
    PRIMARY KEY (id)
) engine=innodb;