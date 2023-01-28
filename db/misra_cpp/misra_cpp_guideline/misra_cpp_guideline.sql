/* misra_cpp_guideline table */
DROP TABLE misra_cpp_guideline;

CREATE TABLE misra_cpp_guideline
(
    idx                      bigint auto_increment primary key,
    created_date             datetime     null,
    last_modified_date       datetime     null,
    created_by_idx           bigint default 0,
    last_modified_by_idx     bigint default 0,
    active_status            varchar(255) null,
    views                    bigint default 0,

    misra_cpp_idx            bigint default 0,
    title                    varchar(255) null,
    hash_tags_idx            bigint default 0,
    project_information_idx  bigint default 0,
    guideline_result         varchar(255) null,
    guideline_result_note    varchar(255) null,
    tool_information_idx     bigint default 0,
    compiler_information_idx bigint default 0,
    content                  mediumtext   null,
    non_compliant_example    mediumtext   null,
    compliant_example        mediumtext   null,
    bad_case_position_list   varchar(255) null,
    good_case_position_list  varchar(255) null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE misra_cpp_guideline
    AUTO_INCREMENT = 1;