/* setting table */
DROP TABLE setting;

CREATE TABLE setting
(
    idx                         bigint auto_increment primary key,
    created_date                datetime     null,
    last_modified_date          datetime     null,
    created_by_idx              bigint default 0,
    last_modified_by_idx        bigint default 0,
    active_status               varchar(255) null,
    views                       bigint default 0,

    total_misra_c_rule_number   bigint default 0,
    total_misra_cpp_rule_number varchar(255) null,
    total_cwe_rule_number       varchar(255) null,

    developer_email             varchar(255) null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE setting
    AUTO_INCREMENT = 1;

/* don't change idx(primary key) */
INSERT INTO setting (idx, created_date, last_modified_date, created_by_idx, last_modified_by_idx, active_status, views, total_misra_c_rule_number, total_misra_cpp_rule_number, total_cwe_rule_number, developer_email)
VALUES (1, '2022-01-04 21:50:15', '2022-01-04 21:50:18', 1, 1, 'ACTIVE', 0, 198, '200', '80', 'scribnote5@gmail.com');
