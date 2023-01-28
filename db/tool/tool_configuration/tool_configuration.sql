/* tool_configuration table */
DROP TABLE tool_configuration;

CREATE TABLE tool_configuration
(
    idx                                     bigint auto_increment primary key,
    created_date                            datetime     null,
    last_modified_date                      datetime     null,
    created_by_idx                          bigint default 0,
    last_modified_by_idx                    bigint default 0,
    active_status                           varchar(255) null,
    views                                   bigint default 0,

    title                                   varchar(255) null,
    priority                                bigint default 0,
    hash_tags_idx                           bigint default 0,
    target_tool_name                        varchar(255) null,
    tool_information_idx                    bigint default 0,
    development_environment_information_idx bigint default 0,
    programming_language                    varchar(255) null,
    ide_information_idx                     bigint default 0,
    compiler_information_idx                bigint default 0,
    operation_environment                   varchar(255) null,
    target_environment_information_idx      bigint default 0,
    target_information_idx                  bigint default 0,
    content                                 mediumtext   null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE tool_configuration
    AUTO_INCREMENT = 1;
