CREATE DATABASE IF NOT EXISTS bd2_tours_19;
USE bd2_tours_19;
DROP USER IF EXISTS 'bd2_dev'@'localhost';
CREATE USER 'bd2_dev'@'localhost' IDENTIFIED WITH mysql_native_password BY 'dev_pass';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP ON bd2_tours_19.* TO 'bd2_dev'@'localhost';
FLUSH PRIVILEGES;