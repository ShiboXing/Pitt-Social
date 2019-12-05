INSERT INTO "profile" (name,email,password,date_of_birth,lastlogin) VALUES ('Fitzgerald Noble','ac.fermentum.vel@massarutrummagna.net','7987','07/15/1998','2019-05-08 04:25:52');
INSERT INTO "profile" (name,email,password,date_of_birth,lastlogin) VALUES ('Neville Owens','augue.ut@ultricesmauris.co.uk','9038','10/19/1998','2020-01-24 07:19:33');
INSERT INTO "profile" (name,email,password,date_of_birth,lastlogin) VALUES ('Hedda Walter','euismod.et.commodo@ut.ca','6488','06/28/1992','2019-07-23 02:25:34');
insert into "friend" (userid1, userid2, jdate, message) values (1,2,'2019-07-23 02:25:34','111');
insert into "friend" (userid1, userid2, jdate, message) values (1,3,'2019-07-23 02:25:34','111');

INSERT INTO "messageinfo" (fromid, message, touserid, togroupid, timesent) VALUES (1,'euismod.et.commodo@ut.ca',2,null,'2019-07-23 02:25:34');
INSERT INTO "messageinfo" (fromid, message, touserid, togroupid, timesent) VALUES (1,'euismod.et.commodo@ut.ca',2,null,'2019-07-23 02:25:34');
INSERT INTO "messageinfo" (fromid, message, touserid, togroupid, timesent) VALUES (1,'euismod.et.commodo@ut.ca',2,null,'2019-07-23 02:25:34');
INSERT INTO "messageinfo" (fromid, message, touserid, togroupid, timesent) VALUES (1,'euismod.et.commodo@ut.ca',2,null,'2019-07-23 02:25:34');
INSERT INTO "messageinfo" (fromid, message, touserid, togroupid, timesent) VALUES (1,'euismod.et.commodo@ut.ca',2,null,'2019-07-23 02:25:34');
INSERT INTO "messageinfo" (fromid, message, touserid, togroupid, timesent) VALUES (1,'euismod.et.commodo@ut.ca',3,null,'2019-07-23 02:25:34');

select topmessages(1,1,'2019-01-23 02:25:34.000000');
select showprofile(1);