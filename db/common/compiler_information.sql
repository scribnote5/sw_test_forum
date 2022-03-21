/* compiler_information table */
DROP TABLE compiler_information;

CREATE TABLE compiler_information
(
    idx           bigint auto_increment primary key,
    table_name    varchar(255) null,
    compiler_name varchar(255) null,
    compiler_note varchar(255) null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE compiler_information
    AUTO_INCREMENT = 1;
