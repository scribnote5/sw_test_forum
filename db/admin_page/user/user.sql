/* user table */
DROP TABLE user;

CREATE TABLE user
(
    idx                  bigint auto_increment primary key,
    created_date         datetime null,
    last_modified_date   datetime null,
    created_by_idx       bigint default 0,
    last_modified_by_idx bigint default 0,
    active_status        varchar(255) null,
    views                bigint default 0,

    username             varchar(255) null,
    password             varchar(255) null,
    name                 varchar(255) null,
    department           varchar(255) null,
    position             varchar(255) null,
    user_status          varchar(255) null,
    authority_type       varchar(255) null,

    contact              varchar(255) null,
    email                varchar(255) null,
    private_email        varchar(255) null,

    introduction         mediumtext null,

    contribution         bigint default 0
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE user
    AUTO_INCREMENT = 1;

/* don't change idx(primary key) */
INSERT INTO user (idx, created_by_idx, created_date, last_modified_by_idx, last_modified_date, active_status, views,
                  username, password, name, department, position, user_status, authority_type,
                  contact, email, private_email,
                  introduction,
                  contribution)
VALUES (1, 1, current_time, 1, current_time, 'ACTIVE', 0,
        'root', '$2a$10$Zg/EzcY/nIqHSPjU3eFpuOopz4Y/1H3LwvVxXhNkvNlni4AOsQ1CW', 'root', 'root', 'K_ETC', 'IN_OFFICE','ROOT',
        '010-0000-0000', 'test@gmail.com', 'test@gmail.com',
        'root',
        0);

/* don't change idx(primary key) */
INSERT INTO user (idx, created_by_idx, created_date, last_modified_by_idx, last_modified_date, active_status, views,
                  username, password, name, department, position, user_status, authority_type,
                  contact, email, private_email,
                  introduction,
                  contribution)
VALUES (2, 1, current_time, 1, current_time, 'ACTIVE', 0,
        'manager', '$2a$10$Zg/EzcY/nIqHSPjU3eFpuOopz4Y/1H3LwvVxXhNkvNlni4AOsQ1CW', 'manager', 'manager', 'K_ETC', 'IN_OFFICE','MANAGER',
        '010-0000-0000', 'test@gmail.com', 'test@gmail.com',
        'manager',
        0);
