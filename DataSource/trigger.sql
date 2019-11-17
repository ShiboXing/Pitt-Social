--Fangzheng Guo, Zhuolun Li, Shibo Xing

set search_path to pitt_social;
--automatically save the ID and recipient of each message to messRecipient
create or replace function saveRecipient() returns trigger as
$$
begin
    insert into messageRecipient values (new.msgID, new.toUserID);
    return new;
end;
$$ language plpgsql;

drop trigger if exists autoSave on messageInfo;
create trigger autoSave
    AFTER
        INSERT
    ON messageInfo
    FOR EACH ROW
execute procedure saveRecipient();

--check if a message is valid, two user cannot send message to each other unless they are friends,
--a user cannot send message to a group unless he is in the group
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

drop trigger if exists validMessage on messageInfo;
create trigger validMessage
    before insert on messageInfo
    for each row
    execute procedure checkValidMessage();

--if 2 users are friends already, they cannot be add to the friend table again (avoid redundancy)
create or replace function ifNewFriends() returns trigger as
$$
declare friendNum int;
begin
    select count(*) into friendNum from friend f where f.userID1 = new.userID2 and f.userID2 = new.userID1;
    if (friendNum > 0) then
        raise exception 'violate constraint already friends';
    end if;
    return new;
end;
$$ language plpgsql;

drop trigger if exists validFriends on friend;
create trigger validFriends
    before
        INSERT
    ON friend
    FOR EACH ROW
execute procedure ifNewFriends();


--Phase 2:
--createUser
drop procedure if exists createuser(user_name varchar(50), email varchar(50), user_password varchar(50)
, user_date_of_birth date, user_lastlogin timestamp);
create procedure createUser(user_name varchar(50), user_email varchar(50),user_password varchar(50),
user_date_of_birth date, user_lastlogin timestamp) as
$$
    begin
        insert into profile(name, email, password, date_of_birth, lastlogin) values(user_name,user_email,user_password,user_date_of_birth,user_lastlogin);
    end;
$$ language plpgsql;

--returnUserName
drop function if exists returnUserName (userid int);
create or replace function returnUserName(user_id int) returns varchar as
    $$
        declare
            Name varchar;
        begin
            select name into Name from profile p where p.user_id = user_id;
            return Name;
        end;
    $$ language plpgsql;

--initiateFriendship

drop procedure if exists initiateFriendship(loginUser int, otherUser int, message varchar(200));
create or replace procedure initiateFriendship(loginUser int, otherUser int, message varchar(200)) as
$$
    begin
        insert into pendingFriend values(loginUser, otherUser, message);
    end;
$$ language plpgsql;

--login
drop function if exists login(inputEmail varchar(50), inputPassword varchar(50));
create or replace function login(inputEmail varchar(50), inputPassword varchar(50)) returns int as
    $$
        declare
            thisUserid varchar;
        begin
            select user_id into thisUserid from profile p where p.email = inputEmail and p.password = inputPassword;
            return thisUserid;
        end;
    $$ language plpgsql;

--createGroup
drop procedure if exists createGroup(thisUserId int, groupName varchar(50), groupSize int, groupDescription varchar(200));
create or replace procedure createGroup(thisUserId int, groupName varchar(50), groupSize int, groupDescription varchar(200)) as
$$
    declare
        thisGroupId int;
    begin
        select nextval(pg_get_serial_sequence('groupinfo','gid')) into thisGroupId;
        --raise notice 'Value: %', thisGroupId;
        insert into groupinfo values(thisGroupId,groupName, groupSize, groupDescription);
        insert into groupmember(gid, userid, role) values (thisgroupid, thisuserid, 'manager');
    end;
$$ language plpgsql;

--initiateAddingGroup
drop procedure if exists initiateAddingGroup(thisUserId int, groupId int, Addinginquiry varchar(200));
create or replace procedure initiateAddingGroup(thisUserId int, groupId int, Addinginquiry varchar(200)) as
$$
    begin
        insert into pendinggroupmember(gid, userid, message) values(groupid, thisuserid, addinginquiry);
    end;
$$ language plpgsql;

--confirmRequests
drop function if exists showFriendRequests(thisuserid int);
create or replace function showFriendRequests(thisuserid int) returns table (tid int) as
    $$
        begin
             return query
                 select fromid from pendingfriend where toid=thisuserid;
        end;
    $$ language plpgsql;

drop function if exists showGroupRequests(thisuserid int);
create or replace function showGroupRequests(thisuserid int) returns table(gid int,uid int) as
    $$
        begin
             return query
                select distinct pgm.gid,userid from pendinggroupmember pgm
                    where pgm.gid in (select distinct gm.gid from groupmember gm
                                    where userid=thisuserid and role='manager');
        end;
    $$ language plpgsql;





