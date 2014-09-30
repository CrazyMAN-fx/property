# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table flat (
  id                        bigint not null,
  action_type               varchar(255),
  category_type             varchar(255),
  rent_kind                 varchar(255),
  rent_price                integer,
  deposit                   integer,
  price                     integer,
  credit                    boolean,
  address                   varchar(255),
  latitude                  float,
  longitude                 float,
  flat_property_kind        varchar(255),
  room_number               integer,
  floor                     integer,
  floor_number              integer,
  material_type             varchar(255),
  overall_sq                integer,
  living_sq                 integer,
  renovation_type           varchar(255),
  security_type             varchar(255),
  balcony                   boolean,
  internet                  boolean,
  own_time                  boolean,
  constraint pk_flat primary key (id))
;

create table sys_user (
  id                        bigint not null,
  user_type                 integer,
  name                      varchar(255),
  contact_person            varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  phone_number              varchar(255),
  constraint ck_sys_user_user_type check (user_type in (0,1)),
  constraint pk_sys_user primary key (id))
;

create sequence flat_seq;

create sequence sys_user_seq;




# --- !Downs

drop table if exists flat cascade;

drop table if exists sys_user cascade;

drop sequence if exists flat_seq;

drop sequence if exists sys_user_seq;

