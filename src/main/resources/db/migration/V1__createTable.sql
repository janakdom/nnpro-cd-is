SET FOREIGN_KEY_CHECKS = 0;

/* Drop Tables */
DROP TABLE IF EXISTS attacked_subjects_incident;
DROP TABLE IF EXISTS railroad;
DROP TABLE IF EXISTS fire_brigade_unit_incident;
DROP TABLE IF EXISTS fire_incident;
DROP TABLE IF EXISTS intervention_type;
DROP TABLE IF EXISTS premise_incident;
DROP TABLE IF EXISTS area;
DROP TABLE IF EXISTS damage_type;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS region;
DROP TABLE IF EXISTS attacked_subject;
DROP TABLE IF EXISTS security_incident;
DROP TABLE IF EXISTS incident;
DROP TABLE IF EXISTS damage;
DROP TABLE IF EXISTS fire_brigade_unit;
DROP TABLE IF EXISTS report;

/* Create Tables */
CREATE TABLE area
(
    id         INT          NOT NULL,
    name       VARCHAR(150) NOT NULL,
    is_deleted bit(1)       NOT NULL
);

CREATE TABLE attacked_subject
(
    id         INT          NOT NULL,
    name       VARCHAR(200) NOT NULL,
    is_deleted bit(1)       NOT NULL
);

CREATE TABLE attacked_subjects_incident
(
    security_incident_id INT NOT NULL,
    attacked_subject_id  INT NOT NULL
);

CREATE TABLE damage
(
    id                   INT          NOT NULL,
    attacked_object      VARCHAR(100) NOT NULL,
    finance_value        FLOAT        NOT NULL,
    damage_type_id       INT          NOT NULL,
    security_incident_id INT          DEFAULT NULL,
    fire_incident_id     INT          DEFAULT NULL,
    is_deleted           bit(1)       NOT NULL
);

CREATE TABLE railroad
(
    id         INT          NOT NULL,
    name       VARCHAR(500) NOT NULL,
    number     VARCHAR(100) NOT NULL,
    is_deleted bit(1)       NOT NULL
);

CREATE TABLE premise_incident
(
    id         INT      NOT NULL,
    valid      DATETIME NOT NULL
);

CREATE TABLE damage_type
(
    id         INT          NOT NULL,
    name       VARCHAR(500) NOT NULL,
    is_deleted bit(1)       NOT NULL
);

CREATE TABLE fire_brigade_unit
(
    id         INT          NOT NULL,
    name       VARCHAR(100) NOT NULL,
    is_deleted bit(1)       NOT NULL
);

CREATE TABLE fire_brigade_unit_incident
(
    security_incident_id INT NOT NULL,
    fire_brigade_unit_id INT NOT NULL
);

CREATE TABLE fire_incident
(
    id                   INT      NOT NULL,
    valid_from           DATETIME NOT NULL,
    valid_to             DATETIME NOT NULL,
    intervention_type_id INT      NOT NULL
);

CREATE TABLE incident
(
    id                   INT           NOT NULL,
    `description`        VARCHAR(5000) DEFAULT NULL,
    creation_Datetime    DATETIME      NOT NULL,
    location             VARCHAR(50)   NOT NULL,
    note                 VARCHAR(5000) NOT NULL,
    premise_incident_id  INT           DEFAULT NULL,
    region_id            INT           NOT NULL,
    security_incident_id INT           DEFAULT NULL,
    owner_id             INT           NOT NULL
);

CREATE TABLE intervention_type
(
    id         INT          NOT NULL,
    name       VARCHAR(500) NOT NULL,
    is_deleted bit(1)       NOT NULL
);

CREATE TABLE role
(
    id         INT         NOT NULL,
    name       VARCHAR(50) NOT NULL,
    is_deleted bit(1)      NOT NULL
);

CREATE TABLE region
(
    id           INT          NOT NULL,
    abbreviation VARCHAR(3)   NOT NULL,
    name         VARCHAR(100) NOT NULL,
    area_id      INT          NOT NULL,
    is_deleted   bit(1)       NOT NULL
);

CREATE TABLE security_incident
(
    id               INT          NOT NULL,
    checked          bit(1)       NOT NULL,
    crime            bit(1)       NOT NULL,
    police           bit(1)       NOT NULL,
    fire_incident_id INT DEFAULT NULL,
    manager_id       INT          NOT NULL,
    railroad_id      INT          NOT NULL,
    carriage_id      INT          NOT NULL
);

CREATE TABLE `user`
(
    id         INT          NOT NULL,
    email      VARCHAR(100) NOT NULL,
    firstname  VARCHAR(50) DEFAULT NULL,
    password   VARCHAR(200) NOT NULL,
    surname    VARCHAR(50) DEFAULT NULL,
    username   VARCHAR(35)  NOT NULL,
    renew_task bit(1)       NOT NULL,
    is_deleted bit(1)       NOT NULL,
    area_id    INT         DEFAULT NULL,
    role_id    INT          NOT NULL
);

CREATE TABLE report
(
    id           int(11)      NOT NULL,
    filename     varchar(50)  NOT NULL,
    name         varchar(150) NOT NULL,
    hash         varchar(64)  NOT NULL,
    description  varchar(500) NOT NULL,
    is_deleted   bit(1)       NOT NULL,
    content      mediumblob   NOT NULL,
    content_type varchar(255) NOT NULL,
    created      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE carriage
(
    id            int(11)     NOT NULL,
    serial_number varchar(50) NOT NULL,
    producer      varchar(75) NOT NULL,
    color         varchar(25),
    home_station  varchar(75) NOT NULL,
    is_deleted    bit(1)      NOT NULL
);

/* Create Indexes */
ALTER TABLE area
    ADD PRIMARY KEY (id),
    ADD UNIQUE KEY UK_226rm1fd8fl8ewh0a7n1k8f94 (name);

ALTER TABLE attacked_subject
    ADD PRIMARY KEY (id),
    ADD UNIQUE KEY UK_m9u2witfmt6mbxoj2mk5gx4ef (name);

ALTER TABLE attacked_subjects_incident
    ADD KEY FKbaupp4g0aux3fjnqrspyn88wf (attacked_subject_id),
    ADD KEY FK2qq08v5ktl69mq2cxn53rtjat (security_incident_id);

ALTER TABLE damage
    ADD PRIMARY KEY (id),
    ADD KEY FKc4branbr61n6y4p0ux4siegmh (damage_type_id),
    ADD KEY FKjfqr2g1jaulmprm0qaxbq479d (security_incident_id);

ALTER TABLE damage_type
    ADD PRIMARY KEY (id),
    ADD UNIQUE KEY UK_f554f5ddsf5d6d54d (name);

ALTER TABLE fire_brigade_unit
    ADD PRIMARY KEY (id),
    ADD UNIQUE KEY UK_23howxobsedsayqdhsxa9n2qa (name);

ALTER TABLE fire_brigade_unit_incident
    ADD KEY FK7aw9gt6mr9lv76srykh1gc5p (fire_brigade_unit_id),
    ADD KEY FK8mnx9s8vdl4p133h3mww5og7f (security_incident_id);

ALTER TABLE fire_incident
    ADD PRIMARY KEY (id),
    ADD KEY FK2oaeiwi7d01bt6cgyl75w58e9 (intervention_type_id);

ALTER TABLE incident
    ADD PRIMARY KEY (id),
    ADD KEY FKta941q2qbcndj5dvpn1gye9bh (premise_incident_id),
    ADD KEY FKswtnug2jge1jwblsyh1nqx8py (region_id),
    ADD KEY FKgsuck247mmo2xae15qc6gqe46 (security_incident_id),
    ADD KEY FKf4564sdf5sd5f4sd65df5sdfs (owner_id);

ALTER TABLE intervention_type
    ADD PRIMARY KEY (id),
    ADD UNIQUE KEY UK_5d54r654sf5e31dsfsdf5f (name);

ALTER TABLE premise_incident
    ADD PRIMARY KEY (id);

ALTER TABLE railroad
    ADD PRIMARY KEY (id),
    ADD UNIQUE KEY UK_51139lfh21ghkyypeuywqno3 (number, name);

ALTER TABLE region
    ADD PRIMARY KEY (id),
    ADD UNIQUE KEY UK_a8y7smfxc30t797e3am3syynq (abbreviation),
    ADD UNIQUE KEY UK_ixr2itih2n9q41fv3qx6mbkrp (name),
    ADD KEY FKdctxym6am8xupghdx4jydkby9 (area_id);

ALTER TABLE role
    ADD PRIMARY KEY (id),
    ADD UNIQUE KEY UK_8sewwnpamngi6b1dwaa88askk (name);

ALTER TABLE security_incident
    ADD PRIMARY KEY (id),
    ADD KEY FKpxoh5rw2x488b4wgbv3p6ikno (fire_incident_id),
    ADD KEY FK4thyyps7mvgqalsf4ghn7j6nn (manager_id),
    ADD KEY FKc5445f5sddfsdf564d5f5sd4f (carriage_id),
    ADD KEY FKc9ieh0f8l0vyrduun4qqswqgc (railroad_id);

ALTER TABLE user
    ADD PRIMARY KEY (id),
    ADD UNIQUE KEY UK_ob8kqyqqgmefl0aco34akdtpe (email),
    ADD UNIQUE KEY UK_sb8bbouer5wak8vyiiy4pf2bx (username),
    ADD KEY FK952enf48r0kesdubmttrmntw (area_id),
    ADD KEY FKn82ha3ccdebhokx3a8fgdqeyy (role_id);

ALTER TABLE report
    ADD PRIMARY KEY (id),
    ADD UNIQUE KEY UK_dw69s56z1ds65e9zi1f5s6s4r (filename),
    ADD UNIQUE KEY UK_fe56sf4e8s9sg6f4sdf65dsez (hash),
    ADD UNIQUE KEY UK_5s4d5fs54d554fd56sd4fd5d6 (name);

ALTER TABLE carriage
    ADD PRIMARY KEY (id),
    ADD UNIQUE KEY UK_df5r8s546d6s4r8th45df6g4f (serial_number);

ALTER TABLE area
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE attacked_subject
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE damage
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE damage_type
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE fire_brigade_unit
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE fire_incident
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE incident
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE intervention_type
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE premise_incident
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE railroad
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE region
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE role
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE security_incident
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE user
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE report
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE carriage
    MODIFY id INT NOT NULL AUTO_INCREMENT;

SET FOREIGN_KEY_CHECKS = 1;
