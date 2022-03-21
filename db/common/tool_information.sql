/* tool_information table */
DROP TABLE tool_information;

CREATE TABLE tool_information
(
    idx        bigint auto_increment primary key,
    table_name varchar(255) null,
    tool_name  varchar(255) null,
    tool_note  varchar(255) null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE tool_information
    AUTO_INCREMENT = 1;
