create table tick (
                      id bigint generated always as identity primary key,
                      symbol varchar(32) not null,
                      price numeric(20, 8) not null ,
                      qty numeric(20, 8) not null,
                      event_time timestamptz not null ,
                      ingest_time timestamptz not null
);
