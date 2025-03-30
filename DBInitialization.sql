CREATE DATABASE proyecto_bd2;
USE proyecto_bd2;
CREATE USER 'bd2_dev'@'localhost' IDENTIFIED WITH mysql_native_password BY 'dev_pass';
GRANT ALL PRIVILEGES ON proyecto_bd2.* TO 'bd2_dev'@'localhost';
FLUSH PRIVILEGES;