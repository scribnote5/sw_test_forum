/* misra_c_example table */
DROP TABLE misra_c_example;

CREATE TABLE misra_c_example
(
    idx                      bigint auto_increment primary key,
    created_date             datetime     null,
    last_modified_date       datetime     null,
    created_by_idx           bigint default 0,
    last_modified_by_idx     bigint default 0,
    active_status            varchar(255) null,
    views                    bigint default 0,

    misra_c_idx              bigint default 0,
    title                    varchar(255) null,
    priority                 bigint default 0,
    tool_information_idx     bigint default 0,
    compiler_information_idx bigint default 0,
    content                  mediumtext   null,
    non_compliant_example    mediumtext   null,
    compliant_example        mediumtext   null,
    bad_case_position_list   varchar(255) null,
    good_case_position_list  varchar(255) null
) DEFAULT CHARSET = UTF8;

ALTER TABLE misra_c_example
    AUTO_INCREMENT = 1;