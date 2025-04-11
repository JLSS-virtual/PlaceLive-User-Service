--liquibase formatted sql

--changeset Jeet:1
--v1 : Creating high-level Database Schema for region and users

CREATE TABLE If Not EXISTS region (
    id BIGINT NOT NULL AUTO_INCREMENT,
    country VARCHAR(255),
    state VARCHAR(255),
    city VARCHAR(255),
    street VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    bio VARCHAR(255) DEFAULT '',
    is_logged_in BOOLEAN,
    email VARCHAR(255),
    mobile_number VARCHAR(255),
    user_region_id BIGINT,
    followers JSON,
    following JSON,
    close_friends JSON,
    profile_image_url VARCHAR(255) DEFAULT '',
    account_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_region_id) REFERENCES region(id)
);
