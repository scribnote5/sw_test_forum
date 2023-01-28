/* development_environment_information table */
DROP TABLE development_environment_information;

CREATE TABLE development_environment_information
(
    idx                          bigint auto_increment primary key,
    table_name                   varchar(255) null,
    development_environment_name varchar(255) null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE development_environment_information
    AUTO_INCREMENT = 1;
