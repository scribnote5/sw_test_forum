/* cwe table */
DROP TABLE cwe;

CREATE TABLE cwe
(
    idx                  bigint auto_increment primary key,
    created_date         datetime     null,
    last_modified_date   datetime     null,
    created_by_idx       bigint default 0,
    last_modified_by_idx bigint default 0,
    active_status        varchar(255) null,
    views                bigint default 0,

    title                varchar(255) null,
    priority             bigint default 0,
    frequency            varchar(255) null,
    hash_tags_idx        bigint default 0,
    static_title         varchar(255) null,
    code_sonar_title     varchar(255) null,
    content              mediumtext   null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE cwe
    AUTO_INCREMENT = 1;