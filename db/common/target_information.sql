/* target_information table */
DROP TABLE target_information;

CREATE TABLE target_information
(
    idx              bigint auto_increment primary key,
    table_name       varchar(255) null,
    board_name       varchar(255) null,
    architecture     varchar(255) null,
    interface_method varchar(255) null,
    debugger_name    varchar(255) null,
    executable_size  varchar(255) null,
    bit              varchar(255) null,
    ram_usage        varchar(255) null,
    ram_free_size    varchar(255) null,
    flash_free_size  varchar(255) null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE target_information
    AUTO_INCREMENT = 1;
