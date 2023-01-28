/* misra_c_guideline_like table */
DROP TABLE misra_c_guideline_like;

CREATE TABLE misra_c_guideline_like
(
    idx                     bigint auto_increment primary key,
    created_date            datetime null,
    last_modified_date      datetime null,
    created_by_idx          bigint default 0,
    last_modified_by_idx    bigint default 0,
    active_status           varchar(255) null,
    views                   bigint default 0,

    misra_c_guideline_idx bigint default 0
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE misra_c_guideline_like
    AUTO_INCREMENT = 1;