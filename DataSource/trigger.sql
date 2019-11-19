--Fangzheng Guo, Zhuolun Li, Shibo Xing

set search_path to pitt_social;
--automatically save the ID and recipient of each message to messRecipient
create or replace function saveRecipient() returns trigger as
$$
begin
    if(new.togroupid is not null) then
        insert into messagerecipient
            select new.msgid,gm.userid from groupmember gm where gm.gid=new.togroupid;
    else
        insert into messageRecipient values (new.msgID, new.toUserID);
    end if;
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
    groupNum   integer;
begin
    if (new.toGroupID is NULL) then
        select count(*)
        into friendsNum
        from (select f.userID1, f.userID2
              from friend as f
              where (f.userID1 = new.fromID and f.userID2 = new.toUserID)
                 or (f.userID2 = new.fromID and f.userID1 = new.toUserID)) as t;
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
    before insert
    on messageInfo
    for each row
execute procedure checkValidMessage();

--if 2 users are friends already, they cannot be add to the friend table again (avoid redundancy)
create or replace function ifNewFriends() returns trigger as
$$
declare
    friendNum int;
begin
    select count(*) into friendNum from friend f
    where f.userID1 = new.userID2 and f.userID2 = new.userID1;
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


create or replace function BeforeUserRemove() returns trigger as
$$
    begin
        if ((select count(*) from profile where user_id=-1)=0) then
            insert into profile values(-1,'dummy','-@-.---','dummy',null,null);
        end if;

        --update groupmember set userid = -1 where userid=old.user_id;
        delete from groupinfo where gid in
                (select gid from groupmember where userid=old.user_id and role='manager');
        update messageinfo set touserid=-1 where touserid=old.user_id and not fromid = -1;
        update messageinfo set fromid=-1 where fromid=old.user_id and not touserid = -1;

        return old;
    end;
$$ language plpgsql;

drop trigger if exists userRemoved on profile;
create trigger userRemoved
    before delete on profile
    for each row
execute function BeforeUserRemove();



--Phase 2:

--createUser
drop procedure if exists createuser(user_name varchar(50), email varchar(50), user_password varchar(50)
    , user_date_of_birth date, user_lastlogin timestamp);
create procedure createUser(user_name varchar(50), user_email varchar(50), user_password varchar(50),
                            user_date_of_birth date, user_lastlogin timestamp) as
$$
begin
    insert into profile(name, email, password, date_of_birth, lastlogin)
    values (user_name, user_email, user_password, user_date_of_birth, user_lastlogin);
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
    insert into pendingFriend values (loginUser, otherUser, message);
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
create or replace procedure createGroup(thisUserId int, groupName varchar(50), groupSize int,
                                        groupDescription varchar(200)) as
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
    insert into pendinggroupmember(gid, userid, message) values (groupid, thisuserid, addinginquiry);
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


drop procedure if exists removeFromPendingFriends(thisUserId int, fromId int);
create or replace procedure removeFromPendingFriends(thisUserId int, fromId int) as
$$
begin
    delete from pendingfriend p where p.fromId = fromId and p.toID = thisUserId;
end;
$$ language plpgsql;
drop procedure if exists removeFromPendingGroupMembers(gid int, fromId int);
create or replace procedure removeFromPendingGroupMembers(gid int, fromId int) as
$$
begin
    delete from pendinggroupmember p where p.gid = gid and p.userid = fromid;
end;
$$ language plpgsql;

--sendMessageToUser
drop function if exists showUserName(userId int);
create or replace function showUserName(userId int) returns varchar as
$$
declare
    name varchar;
begin
    select p.name into name from profile p where p.user_id = userid;
    return name;
end;
$$ language plpgsql;

drop procedure if exists createMessage(thisUserId int, recipient int, content varchar, sendtime timestamp);
create or replace procedure createMessage(thisUserId int, recipient int, content varchar, sendtime timestamp) as
$$
begin
    insert into messageInfo(fromId, message, toUserId, toGroupId, timesent)
    values (thisuserid, content, recipient, null, sendtime);
end;
$$ language plpgsql;

drop function if exists ifSendFriendSuccessfully (thisUserId int, recipient int, content varchar, sendTime timestamp);
create or replace function ifSendFriendSuccessfully(thisUserId int, recipient int, content varchar, sendTime timestamp) returns boolean as
$$
declare
    result boolean;
begin
    result = exists(select msgid
                    from messageinfo
                    where fromid = thisuserid
                      and message = content
                      and touserid = recipient
                      and timesent = sendTime);
    return result;
end;
$$ language plpgsql;

--sendMessageToGroup
drop function if exists ifInGroup (thisUserId int, gid int);
create or replace function ifInGroup(thisUserId int, gid int) returns boolean as
$$
declare
    result boolean;
begin
    result = exists(select gm.gid, gm.userid from groupmember gm where gm.userid = thisuserid and gm.gid = gid);
    return result;
end;
$$ language plpgsql;

drop function if exists showGroupName(gid int);
create or replace function showGroupName(gid int) returns varchar as
$$
declare
    name varchar;
begin
    select g.name into name from groupinfo g where g.gid = gid;
    return name;
end;
$$ language plpgsql;

drop procedure if exists createMessage(thisUserId int, recipientgroup int, content varchar, sendtime timestamp);
create or replace procedure createMessage(thisUserId int, recipientgroup int, content varchar, sendtime timestamp) as
$$
begin
    insert into messageInfo(fromId, message, toUserId, toGroupId, timesent)
    values (thisuserid, content, null, recipientgroup, sendtime);
end;
$$ language plpgsql;

drop function if exists ifSendGroupSuccessfully (thisUserId int, recipientgroup int, content varchar, sendTime timestamp);
create or replace function ifSendGroupSuccessfully(thisUserId int, recipientgroup int, content varchar,
                                                   sendTime timestamp) returns boolean as
$$
declare
    result boolean;
begin
    result = exists(select msgid
                    from messageinfo
                    where fromid = thisuserid
                      and message = content
                      and togroupid = recipientgroup
                      and timesent = sendTime);
    return result;
end;
$$ language plpgsql;


--displayMessages
drop function if exists displayMessages(thisuserid int);
create or replace function displayMessages(thisuserid int)
    returns table
            (
                msgID     int,
                fromID    int,
                message   varchar(200),
                toUserID  int,
                toGroupID int,
                timeSent  timestamp
            )
as
$$
begin
    return query
        select *
        from MessageInfo
        where msgid in (
            select mr.msgID
            from messagerecipient mr
            where mr.userid = thisuserid);
end;
$$ language plpgsql;

--displayNewMessages
drop function if exists displayNewMessages(thisuserid int);
create or replace function displayNewMessages(thisuserid int)
    returns table
            (
                msgID     int,
                fromID    int,
                message   varchar(200),
                toUserID  int,
                toGroupID int,
                timeSent  timestamp
            )
as
$$
begin
    return query
        select *
        from MessageInfo
        where msgid in (
            select mr.msgID
            from messagerecipient mr
            where mr.userid = thisuserid)
          and timesent > (select lastlogin from profile where user_id = thisuserid);
end;
$$ language plpgsql;

--displayFriends
drop function if exists returnFriendsList(thisuserid int);
create or replace function returnFriendsList(thisuserid int)
    returns table
            (
                friendID   int,
                friendName varchar(50)
            )
as
$$
begin
    return query
        select id.userid, p.name
        from ((select userID1 from friend where UserId2 = thisuserid)
              union
              (select userID2 from friend where UserId1 = thisuserid)) as id(userid)
                 left outer join profile p on id.userid = p.user_id;
end;
$$ language plpgsql;

drop function if exists iffriends(thisuserid int, checkid int);
create or replace function iffriends(thisuserid int, checkid int) returns boolean as
$$
declare
    output boolean;
begin
    /*output = checkid in (select id.userid
                         from ((select userID1 from friend where UserId2 = thisuserid)
                               union
                               (select userID2 from friend where UserId1 = thisuserid)) as id(userid)
                                  left outer join profile p on id.userid = p.user_id);*/
    output = (select count(*) from friend
        where (userid1=thisuserid and userid2=checkid)
           or (userid2=thisuserid and userid1=checkid))>0;
    return output;
end;
$$ language plpgsql;

drop function if exists showProfile(friendid int);
create or replace function showProfile(friendid int)
    returns table
            (
                user_id       int,
                name          varchar(50),
                email         varchar(50),
                date_of_birth date

            )
as
$$
begin
    return query
        select user_id, name, email, date_of_birth
        from profile
        where user_id = friendid;
end;
$$ language plpgsql;

--searchForUser
/*drop function if exists searchForUser(keyword varchar);
create or replace function searchForUser(keyword varchar) returns table(user_id int,name varchar,email varchar,password varchar,date_of_birth date,lastlogin timestamp) as
$$
begin
return query select * from profile where keyword in name and keyword in email;
end;
$$language plpgsql;
  */

--threeDegrees
--use returnFriendsList (thisuserid int)

-- --threeDegrees
-- create or replace function threeDegrees(start_uid int,end_uid int)
-- returns table(uid1 int,uid2 int,uid3 int,uid4 int) as
-- $$
--     begin
--         select userid2 from friend where start_uid=userid1;
--         return null;
--     end;
-- $$ language plpgsql;

--topMessages
drop function if exists topMessagesRecievedFrom(thisuserid int, k int, currentTimeMinusSixMonth timestamp);
create or replace function topMessagesRecievedFrom(thisuserid int, k int, currentTimeMinusSixMonth timestamp)
    returns table
            (
                userid int,
                name   int
            )
as
$$
begin
    return query select touserid
                 from messageinfo
                 where fromid = thisuserid
                   and togroupid = null
                   and timesent > currentTimeMinusSixMonth
                 group by touserid
                 order by count(msgid) desc
                 limit k;
end;
$$ language plpgsql;

drop function if exists topMessagesSentTo(thisuserid int, k int, currentTimeMinusSixMonth timestamp);
create or replace function topMessagesSentTo(thisuserid int, k int, currentTimeMinusSixMonth timestamp)
    returns table
            (
                userid int,
                name   int
            )
as
$$
begin
    return query select fromid
                 from messageinfo
                 where touserid = thisuserid
                   and togroupid = null
                   and timesent > currentTimeMinusSixMonth
                 group by fromid
                 order by count(msgid) desc
                 limit k;
end;
$$ language plpgsql;

--logout
drop procedure if exists logout(thisuserid int, currentTime timestamp);
create or replace procedure logout(thisuserid int, currentTime timestamp) as
$$
begin
    update profile set lastlogin = currenttime where user_id = thisuserid;
end;
$$ language plpgsql;


--test save group recipients trigger
/*
insert into groupmember values(1,4,'lali');
insert into groupmember values(1,2,'blu blu');
insert into messageinfo(fromid, message, touserid, togroupid, timesent)
values(3,'ewww!',null,1,'2019-05-08 04:25:52');

insert into friend values(1,2,'2019-05-02','dang');
insert into messageinfo(fromid, message, touserid, togroupid, timesent)
values(1,'ewww!',2,null,'2019-05-08 04:25:52');*/


--test UserRemoved trigger
--delete from profile where user_id=4;

--test save group recipients trigger
/*
insert into groupmember values(1,4,'lali');
insert into groupmember values(1,2,'blu blu');
insert into messageinfo(fromid, message, touserid, togroupid, timesent)
values(3,'ewww!',null,1,'2019-05-08 04:25:52');

insert into friend values(1,2,'2019-05-02','dang');
insert into messageinfo(fromid, message, touserid, togroupid, timesent)
values(1,'ewww!',2,null,'2019-05-08 04:25:52');*/

--test UserRemoved trigger
--delete from profile where user_id=4;


--test BeforeUserDelete trigger
-- insert into friend values(3,2,'2019-05-02','dang');
-- insert into friend values(3,1,'2019-05-02','dang');
-- insert into friend values(1,4,'2019-05-02','dang');
-- select * from iffriends(3,2);
-- select * from iffriends(2,3);
-- select * from iffriends(3,4);
-- select * from iffriends(4,4);

-- insert into groupmember values(3,3,'lali');
-- insert into messageinfo(fromid, message, touserid, togroupid, timesent) values (3,'qw',1,null,null);
-- insert into messageinfo(fromid, message, touserid, togroupid, timesent) values (3,'qw',2,null,null);
-- delete from profile where user_id=3;
-- delete from profile where user_id=2;
-- delete from profile where user_id=1;

