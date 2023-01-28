/* question_answer table */
DROP TABLE question_answer;

CREATE TABLE question_answer
(
    idx                  bigint auto_increment primary key,
    created_date         datetime null,
    last_modified_date   datetime null,
    created_by_idx       bigint default 0,
    last_modified_by_idx bigint default 0,
    active_status        varchar(255) null,
    views                bigint default 0,

    group_idx            bigint default 0,
    title                varchar(255) null,
    type                 varchar(255) null,
    content              mediumtext null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE question_answer
    AUTO_INCREMENT = 1;