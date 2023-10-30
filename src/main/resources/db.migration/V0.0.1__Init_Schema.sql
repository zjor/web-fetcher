create table request
(
    id                 bigserial not null,
    created_date       timestamp not null,
    last_modified_date timestamp not null,
    url                text      not null,
    content_length     bigint    not null,
    webhook_url        text      not null,
    last_status        text      not null,
    error              text      not null,
    primary key (id)
);
