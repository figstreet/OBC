
CREATE TABLE "client" (
    clid int NOT NULL PRIMARY KEY,
    cl_name varchar_ignorecase(200) NOT NULL,
    cl_active bit NOT NULL default 1,
    cl_added_by int NOT NULL,
    cl_added_dt datetime NOT NULL DEFAULT NOW(),
    cl_lastupdated_by int,
    cl_lastupdated_dt datetime
);

insert into client (clid, cl_name, cl_active, cl_added_by, cl_lastupdated_by, cl_lastupdated_dt)
values (0, 'GENERAL', 1, 100, 100, NOW());

