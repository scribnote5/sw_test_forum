/* ide_information table */
DROP TABLE ide_information;

CREATE TABLE ide_information
(
    idx        bigint auto_increment primary key,
    table_name varchar(255) null,
    ide_name   varchar(255) null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE ide_information
    AUTO_INCREMENT = 1;
