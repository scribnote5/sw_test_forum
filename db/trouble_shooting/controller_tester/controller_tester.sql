/* controller_tester_tool table */
DROP TABLE controller_tester_tool;

CREATE TABLE controller_tester_tool
(
    idx                      bigint auto_increment primary key,
    created_date             datetime null,
    last_modified_date       datetime null,
    created_by_idx           bigint default 0,
    last_modified_by_idx     bigint default 0,
    active_status            varchar(255) null,
    views                    bigint default 0,

    title                    varchar(255) null,
    priority                 bigint default 0,
    hash_tags_idx            bigint default 0,
    tool_category            varchar(255) null,
    tool_information_idx     bigint default 0,
    ide_information_idx      bigint default 0,
    compiler_information_idx bigint default 0,
    content                  mediumtext null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE controller_tester_tool
    AUTO_INCREMENT = 1;
