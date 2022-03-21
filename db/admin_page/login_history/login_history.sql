/* login_history table */
DROP TABLE login_history;

CREATE TABLE login_history
(
    idx                  bigint auto_increment primary key,
    created_date         datetime     null,
    last_modified_date   datetime     null,
    created_by_idx       bigint default 0,
    last_modified_by_idx bigint default 0,
    active_status        varchar(255) null,
    views                bigint default 0,

    login_username       varchar(255),
    ip                   varchar(255),
    location             varchar(255),
    message              varchar(255),
    detailed_message     varchar(255),
    login_result_type    varchar(255)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE login_history
    AUTO_INCREMENT = 1;