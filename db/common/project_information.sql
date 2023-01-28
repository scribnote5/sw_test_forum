/* project_information table */
DROP TABLE project_information;

CREATE TABLE project_information
(
    idx          bigint auto_increment primary key,
    table_name   varchar(255) null,
    project_name varchar(255) null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE project_information
    AUTO_INCREMENT = 1;
