--liquibase formatted sql logicalFilePath:update.mysql.sql

--changeset avillalobos:some-tables-2018-MAR-9-14-08
--comment creating few tables to start with
-- -----------------------------------------------------
-- Table `ers_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ers_user` (
  `id` CHAR(36) NOT NULL,
  `email` VARCHAR(128) NOT NULL,
  `password` VARCHAR(256) NOT NULL,
  `first_name` VARCHAR(128) NOT NULL,
  `last_name` VARCHAR(128) NOT NULL,
  `type` ENUM('ADMIN', 'ORGANIZER', 'USER') NOT NULL,
  `status` ENUM('ENABLED', 'DISABLED', 'LOCKED') NOT NULL,
  `failed_login_attempts` INT NOT NULL DEFAULT 0,
  `date_created` DATETIME NOT NULL,
  `date_modified` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `category` (
  `id` INT NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `role` (
  `id` INT NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_profile`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_profile` (
  `user_id` CHAR(36) NOT NULL,
  `government_id` VARCHAR(45) NOT NULL,
  `external_id` VARCHAR(45) NULL,
  `hand` ENUM('LEFT', 'RIGHT') NOT NULL,
  `team` VARCHAR(128) NULL,
  `date_birth` DATE NOT NULL,
  `age` INT NOT NULL,
  `phone` VARCHAR(45) NOT NULL,
  `genre` ENUM('MALE', 'FEMALE') NOT NULL,
  `weight` CHAR(6) NULL,
  `height` CHAR(3) NULL,
  `country` VARCHAR(128) NOT NULL,
  `state` VARCHAR(128) NULL,
  `city` VARCHAR(128) NULL,
  `street1` VARCHAR(512) NULL,
  `street2` VARCHAR(512) NULL,
  `emergency_contact_name` VARCHAR(128) NOT NULL,
  `emergency_contact_phone` VARCHAR(45) NOT NULL,
  `shirt_size` ENUM('XS', 'S', 'M', 'L', 'XL', '2XL') NOT NULL,
  `category_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`user_id`),
  INDEX `idx_fk_user_profile_category` (`category_id` ASC),
  INDEX `idx_fk_user_profile_role` (`role_id` ASC),
  CONSTRAINT `fk_user_profile_ers_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `ers_user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_profile_category`
    FOREIGN KEY (`category_id`)
    REFERENCES `category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_profile_role`
    FOREIGN KEY (`role_id`)
    REFERENCES `role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `discipline`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `discipline` (
  `id` INT NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_profile_has_discipline`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_profile_has_discipline` (
  `user_id` CHAR(36) NOT NULL,
  `discipline_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `discipline_id`),
  INDEX `idx_fk_user_profile_has_discipline_discipline` (`discipline_id` ASC),
  INDEX `idx_fk_user_profile_has_discipline_user_profile` (`user_id` ASC),
  CONSTRAINT `fk_user_profile_has_discipline_user_profile`
    FOREIGN KEY (`user_id`)
    REFERENCES `user_profile` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_profile_has_discipline_discipline`
    FOREIGN KEY (`discipline_id`)
    REFERENCES `discipline` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_profile_picture`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_profile_picture` (
  `id` CHAR(36) NOT NULL,
  `width` INT NOT NULL,
  `height` INT NOT NULL,
  `picture` MEDIUMBLOB NOT NULL,
  `user_id` CHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_fk_user_profile_picture_user_profile` (`user_id` ASC),
  CONSTRAINT `fk_user_profile_picture_user_profile1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user_profile` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `event` (
  `id` CHAR(36) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `description` TEXT NULL,
  `created_by_user_id` CHAR(36) NOT NULL,
  `created_by_user_email` VARCHAR(128) NOT NULL,
  `date_created` DATETIME NOT NULL,
  `date_modified` DATETIME NOT NULL,
  `date_start` DATETIME NOT NULL,
  `date_end` DATETIME NOT NULL,
  `date_limit_register` DATETIME NOT NULL,
  `country` VARCHAR(128) NOT NULL,
  `state` VARCHAR(128) NOT NULL COMMENT '	',
  `city` VARCHAR(128) NOT NULL,
  `street1` VARCHAR(512) NULL,
  `street2` VARCHAR(512) NULL,
  `website` VARCHAR(128) NULL,
  `social_network_fb` VARCHAR(128) NULL,
  `social_network_ig` VARCHAR(128) NULL,
  `social_network_tw` VARCHAR(128) NULL,
  `email` VARCHAR(128) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `event_picture`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `event_picture` (
  `id` CHAR(36) NOT NULL,
  `width` INT NOT NULL,
  `height` INT NOT NULL,
  `picture` MEDIUMBLOB NOT NULL,
  `event_id` CHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_fk_event_picture_event` (`event_id` ASC),
  CONSTRAINT `fk_event_picture_event`
    FOREIGN KEY (`event_id`)
    REFERENCES `event` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `event_registration_option`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `event_registration_option` (
  `id` CHAR(36) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `description` TEXT NOT NULL,
  `registration_cost` DOUBLE NOT NULL,
  `registration_cost_currency` ENUM('CRC', 'USD') NOT NULL,
  `event_id` CHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_fk_event_registration_option_event` (`event_id` ASC),
  CONSTRAINT `fk_event_registration_option_event`
    FOREIGN KEY (`event_id`)
    REFERENCES `event` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_join_event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_join_event` (
  `user_id` CHAR(36) NOT NULL,
  `event_id` CHAR(36) NOT NULL,
  PRIMARY KEY (`user_id`, `event_id`),
  INDEX `idx_fk_ers_user_has_event_event` (`event_id` ASC),
  INDEX `idx_fk_ers_user_has_event_ers_user` (`user_id` ASC),
  CONSTRAINT `fk_ers_user_has_event_ers_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `ers_user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ers_user_has_event_event`
    FOREIGN KEY (`event_id`)
    REFERENCES `event` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `security_token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `security_token` (
  `id` CHAR(36) NOT NULL,
  `date_created` DATETIME NOT NULL,
  `date_last_access` VARCHAR(45) NOT NULL,
  `host_ip` VARCHAR(45) NULL,
  `user_id` CHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_fk_security_token_ers_user` (`user_id` ASC),
  CONSTRAINT `fk_security_token_ers_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `ers_user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

--changeset avillalobos:user-profile-non-required-fields-2018-MAR-13-12-16
--comment realised some fields of the user profile are not required in the db at creation time
ALTER TABLE `user_profile` 
CHANGE COLUMN `government_id` `government_id` VARCHAR(45) NULL ,
CHANGE COLUMN `hand` `hand` ENUM('LEFT', 'RIGHT') NULL ,
CHANGE COLUMN `date_birth` `date_birth` DATE NULL ,
CHANGE COLUMN `age` `age` INT(11) NULL ,
CHANGE COLUMN `phone` `phone` VARCHAR(45) NULL ,
CHANGE COLUMN `genre` `genre` ENUM('MALE', 'FEMALE') NULL ,
CHANGE COLUMN `country` `country` VARCHAR(128) NULL ,
CHANGE COLUMN `emergency_contact_name` `emergency_contact_name` VARCHAR(128) NULL ,
CHANGE COLUMN `emergency_contact_phone` `emergency_contact_phone` VARCHAR(45) NULL ,
CHANGE COLUMN `shirt_size` `shirt_size` ENUM('XS', 'S', 'M', 'L', 'XL', '2XL') NULL ,
CHANGE COLUMN `category_id` `category_id` INT(11) NULL ,
CHANGE COLUMN `role_id` `role_id` INT(11) NULL ;

--changeset avillalobos:user-profile-shirt-size-values-2018-MAR-13-12-1625
--comment 2XL is not valid in java changing to XL2
ALTER TABLE `user_profile` 
CHANGE COLUMN `shirt_size` `shirt_size` ENUM('XS', 'S', 'M', 'L', 'XL', 'XL2') NULL DEFAULT NULL ;

--changeset avillalobos:roles-2018-MAR-13-15-05
--comment insert some roles
INSERT INTO `role` (`id`, `name`) VALUES ('1', 'Director Técnico');
INSERT INTO `role` (`id`, `name`) VALUES ('2', 'Entrenador');
INSERT INTO `role` (`id`, `name`) VALUES ('3', 'Mecánico');
INSERT INTO `role` (`id`, `name`) VALUES ('4', 'Nutricionista');
INSERT INTO `role` (`id`, `name`) VALUES ('5', 'Masajista');
INSERT INTO `role` (`id`, `name`) VALUES ('6', 'Dirigente');
INSERT INTO `role` (`id`, `name`) VALUES ('7', 'Delegado');
INSERT INTO `role` (`id`, `name`) VALUES ('8', 'Comisario Nacional Élite');
INSERT INTO `role` (`id`, `name`) VALUES ('9', 'Comisario Nacional');
INSERT INTO `role` (`id`, `name`) VALUES ('10', 'Comisario Regional');
INSERT INTO `role` (`id`, `name`) VALUES ('11', 'Chofer');
INSERT INTO `role` (`id`, `name`) VALUES ('12', 'Asistente de Equipo');
INSERT INTO `role` (`id`, `name`) VALUES ('13', 'Staff');
INSERT INTO `role` (`id`, `name`) VALUES ('14', 'Prensa');
INSERT INTO `role` (`id`, `name`) VALUES ('15', 'Administrativo');
INSERT INTO `role` (`id`, `name`) VALUES ('16', 'Organizador');

--changeset avillalobos:categories-2018-MAR-13-15-08
--comment insert some categories
INSERT INTO `category` (`id`, `name`) VALUES ('1', 'Sub-electrón');
INSERT INTO `category` (`id`, `name`) VALUES ('2', 'Electrón');
INSERT INTO `category` (`id`, `name`) VALUES ('3', 'Átomo');
INSERT INTO `category` (`id`, `name`) VALUES ('4', 'Pre-infantil');
INSERT INTO `category` (`id`, `name`) VALUES ('5', 'Infantil');
INSERT INTO `category` (`id`, `name`) VALUES ('6', 'Pre-juvenil');
INSERT INTO `category` (`id`, `name`) VALUES ('7', 'Juvenil');
INSERT INTO `category` (`id`, `name`) VALUES ('8', 'Sub-23');
INSERT INTO `category` (`id`, `name`) VALUES ('9', 'Élite');
INSERT INTO `category` (`id`, `name`) VALUES ('10', 'Máster');

--changeset avillalobos:disciplines-2018-MAR-13-15-11
--comment insert some disciplines
INSERT INTO `discipline` (`id`, `name`) VALUES ('1', 'Ruta');
INSERT INTO `discipline` (`id`, `name`) VALUES ('2', 'MTB');
INSERT INTO `discipline` (`id`, `name`) VALUES ('3', 'Pista');
INSERT INTO `discipline` (`id`, `name`) VALUES ('4', 'Bicicross');
INSERT INTO `discipline` (`id`, `name`) VALUES ('5', 'Downhill');
INSERT INTO `discipline` (`id`, `name`) VALUES ('6', 'Libre');
INSERT INTO `discipline` (`id`, `name`) VALUES ('7', 'Recreativo');
INSERT INTO `discipline` (`id`, `name`) VALUES ('8', 'Indoor');

--changeset avillalobos:security-token-last-access-date-fix-2018-MAR-13-22-11
--comment date last access was a string by mistake
ALTER TABLE `security_token` 
CHANGE COLUMN `date_last_access` `date_last_access` DATETIME NOT NULL ;

--changeset avillalobos:default-users-2018-MAR-14-14-55
--comment Adding some default users with password 1234
INSERT INTO `ers_user` (`id`,`email`,`password`,`first_name`,`last_name`,`type`,`status`,`failed_login_attempts`,`date_created`,`date_modified`)
VALUES ('1','alevilla86+admin@hotmail.com','$2a$12$TycjP54XU..UXi5mVXBw8eoeF6tY67n9E/SDGelyGVa.y5Xi0w8hi','Admin','One','ADMIN','ENABLED','0',now(),now());
INSERT INTO `user_profile` (`user_id`) VALUES ('1');

INSERT INTO `ers_user` (`id`,`email`,`password`,`first_name`,`last_name`,`type`,`status`,`failed_login_attempts`,`date_created`,`date_modified`)
VALUES ('2','alevilla86+organizer@hotmail.com','$2a$12$TycjP54XU..UXi5mVXBw8eoeF6tY67n9E/SDGelyGVa.y5Xi0w8hi','Organizer','One','ORGANIZER','ENABLED','0',now(),now());
INSERT INTO `user_profile` (`user_id`) VALUES ('2');

INSERT INTO `ers_user` (`id`,`email`,`password`,`first_name`,`last_name`,`type`,`status`,`failed_login_attempts`,`date_created`,`date_modified`)
VALUES ('3','alevilla86+user@hotmail.com','$2a$12$TycjP54XU..UXi5mVXBw8eoeF6tY67n9E/SDGelyGVa.y5Xi0w8hi','User','One','USER','ENABLED','0',now(),now());
INSERT INTO `user_profile` (`user_id`) VALUES ('3');

--changeset avillalobos:adding-missing-role-2018-MAR-15-08-42
--comment runner role was missing
INSERT INTO `role` (`id`, `name`) VALUES ('17', 'Corredor');