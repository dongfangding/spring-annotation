CREATE DATABASE `spring-annotation` CHARACTER SET utf8 COLLATE utf8_general_ci;

USE spring-annotation;

DROP TABLE IF EXISTS USER;
CREATE TABLE USER(
	ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	NAME VARCHAR(64),
	PASSWORD VARCHAR(64),
	BIRTH_DAY DATE,
	TEL VARCHAR(32),
  ADDRESS VARCHAR(200)
);