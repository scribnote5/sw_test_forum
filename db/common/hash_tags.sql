/* hash_tags table */
DROP TABLE hash_tags;

CREATE TABLE hash_tags
(
    idx              bigint auto_increment primary key,
    table_name       varchar(255) null,
    content          varchar(255) null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE hash_tags
    AUTO_INCREMENT = 1;