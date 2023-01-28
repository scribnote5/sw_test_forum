/* notice_attached_file table */
DROP TABLE notice_attached_file;

CREATE TABLE notice_attached_file
(
    idx             bigint auto_increment primary key,
    created_by_idx  bigint       null,
    created_date    datetime     null,
    file_name       varchar(255) null,
    saved_file_name varchar(255) null,
    file_size       bigint       null,
    download_count  bigint       null,
    notice_idx      bigint       null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE notice_attached_file
    AUTO_INCREMENT = 1;