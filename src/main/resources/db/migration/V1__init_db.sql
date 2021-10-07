create TABLE `User`
             (
                          id            BIGINT NOT NULL auto_increment,
                          first_name    VARCHAR(255),
                          second_name   VARCHAR(255),
                          email         VARCHAR(255) UNIQUE,
                          phone_number  VARCHAR(255) UNIQUE,
                          password      VARCHAR(255),
                          profile_photo VARCHAR(255),
                          location      VARCHAR(255),
                          registered_at DATETIME(6),
                          last_login    datetime(6),
                          status        VARCHAR(255),
                          role          VARCHAR(255),
                          PRIMARY KEY (id)
             )
             engine=innodb;

create TABLE `Ad`
             (
                          id           BIGINT NOT NULL auto_increment,
                          author_id    BIGINT NOT NULL,
                          title        VARCHAR(255),
                          description  VARCHAR(255),
                          category_id  BIGINT NOT NULL,
                          price        DECIMAL(19,2),
                          release_year DATE,
                          created_at   DATETIME(6),
                          status       VARCHAR(255),
                          PRIMARY KEY (id),
                          CONSTRAINT fk_author_ad FOREIGN KEY (author_id) REFERENCES user (id)
             )
             engine=innodb;

create TABLE `Ad_Photo`
              (
                         `id`    BIGINT NOT NULL auto_increment,
                         `ad_id` BIGINT NOT NULL,
                         `photo` VARCHAR(255),
                         PRIMARY KEY (`id`),
                         CONSTRAINT `fk_photos_ad` FOREIGN KEY (`ad_id`) REFERENCES ad (id)
              )
              engine=innodb;

create TABLE `Category`
              (
                         `id`            BIGINT(19) NOT NULL auto_increment,
                         `category_name` VARCHAR(255) NOT NULL,
                         `parent_id`     BIGINT(19) NULL DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         CONSTRAINT `fk_parent_children` FOREIGN KEY (`parent_id`) REFERENCES
                         category (`id`)
              )
              engine=innodb;

