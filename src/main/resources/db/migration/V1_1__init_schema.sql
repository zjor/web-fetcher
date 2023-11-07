create table if not exists request
(
    id                 uuid      not null,
    created_date       timestamp not null,
    last_modified_date timestamp not null,
    url                text      not null,
    download_url       text,
    content_length     bigint,
    webhook_url        text,
    last_status        text      not null,
    error              text,
    primary key (id)
);
