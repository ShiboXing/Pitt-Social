
--initialize schema and alter session
drop schema if exists pitt_social cascade;
create schema pitt_social;
set search_path to pitt_social;

drop table if exists profile cascade;
drop table if exists friend cascade;
drop table if exists pendingFriend cascade;
drop table if exists message cascade;
drop table if exists messageRecipient cascade;
drop table if exists "group" cascade;
drop table if exists groupMember cascade;
drop table if exists pendingGroupMember cascade;


create table profile
(
    user_id int,
    name varchar(50),
    email varchar(50),
    password varchar(50),
    date_of_birth date,
    lastlogin timestamp,
    constraint profile_pk primary key (user_id) not deferrable
);

create table friend
(
    userID1 int,
    userID2 int,
    JDate date,
    message varchar(200),
    constraint friend_pk primary key (userID1,userID2) not deferrable,
    constraint profile_fk1 foreign key (userID1) references profile(user_id),
    constraint profile_fk2 foreign key (userID2) references profile(user_id)
);

create table pendingFriend
(
    fromID int,
    toID int,
    message varchar(200),
    constraint pendingFriend_pk primary key (fromID, toID) not deferrable,
    constraint pendingFriend_fk1 foreign key (fromID) references profile(user_id),
    constraint pendingFriend_fk2 foreign key (toID) references profile(user_id)
);

create table message
(
    msgID int,
    fromID int,
    message varchar(200),
    toUserID int default NULL,
    toGroupID int default NULL,
    timeSent timestamp,
    constraint message_pk primary key (msgID) not deferrable,
    constraint message_fk1 foreign key (fromID) references profile(user_id),
    constraint message_fk2 foreign key (toUserID) references profile(user_id),
    constraint validMessage check ((toUserID is null and toGroupID is not null) or (toUserID is not null and toGroupID is null))
);

create table messageRecipient
(
    msgID int,
    userID int,
    constraint messageRecipient_pk primary key (msgID,userID) not deferrable,
    constraint messageRecipient_fk1 foreign key (msgID) references message (msgID),
    constraint messageRecipient_fk2 foreign key (userID) references profile(user_id)
);

create table "group"
(
    gID int,
    name varchar(50),
    "limit" int,
    description varchar(200),
    constraint group_pk primary key (gID) not deferrable,
    constraint group_ck check ("limit">0)
);

create table groupMember
(
    gId int,
    userId int,
    role varchar(20),
    constraint groupMember_pk primary key (gID,userId) not deferrable,
    constraint groupMember_fk1 foreign key (gId) references "group"(gID),
    constraint groupMember_fk2 foreign key (userId) references profile(user_id)
);

create table pendingGroupMember
(
    gID int,
    userId int,
    message varchar(200),
    constraint pendingGroupMember_pk primary key (gID,userId) not deferrable,
    constraint pendingGroupMember_fk1 foreign key (gId) references "group"(gID),
    constraint pendingGroupMember_fk2 foreign key (userId) references profile(user_id)
);

alter table message
    add constraint message_fk3 foreign key (toGroupID) references "group"(gID);

create or replace function saveRecipient() returns trigger as
$$
begin
    insert into messageRecipient values (new.msgID, new.toUserID);
    return new;
end;
$$ language plpgsql;

drop trigger if exists autoSave on message;
create trigger autoSave
    AFTER
        INSERT
    ON message
    FOR EACH ROW
execute procedure saveRecipient();

create or replace function checkValidMessage() returns trigger as
    $$
    declare
        friendsNum integer;
        groupNum integer;
    begin
        if (new.toGroupID is NULL) then
            select count(*) into friendsNum from (select f.userID1, f.userID2 from friend as f where (f.userID1 = new.fromID and f.userID2 = new.toUserID) or (f.userID2 = new.fromID and f.userID1 = new.toUserID)) as t;
            if (friendsNum > 0) then
            return new;
            else
            raise exception 'violate constraint areFriends';
            end if;
        else
            select count(*) into groupNum from groupMember g where g.gId = new.toGroupID and g.userId = new.fromID;
            if (groupNum > 0) then
            return new;
            else
            raise exception 'violate constraint inGroup';
            end if;
        end if;
    end;
    $$ LANGUAGE plpgsql;

drop trigger if exists validMessage on message;
create trigger validMessage
    before insert on message
    for each row
    execute procedure checkValidMessage();






