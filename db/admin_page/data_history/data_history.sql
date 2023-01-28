/* data_history table */
DROP TABLE data_history;

CREATE TABLE data_history
(
    idx                  bigint auto_increment primary key,
    created_date         datetime     null,
    last_modified_date   datetime     null,
    created_by_idx       bigint default 0,
    last_modified_by_idx bigint default 0,
    active_status        varchar(255) null,
    views                bigint default 0,

    audit_idx            bigint,
    audit_board          varchar(255),
    audit_type           varchar(255),
    message              varchar(255),
    detailed_message     varchar(255),
    content              mediumtext
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE data_history
    AUTO_INCREMENT = 1;