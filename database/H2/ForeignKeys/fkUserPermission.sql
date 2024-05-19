
ALTER TABLE "userpermission" DROP CONSTRAINT if exists "FK_users_userpermission_added";
ALTER TABLE "userpermission" DROP CONSTRAINT if exists "FK_users_userpermission_lastupdated";
ALTER TABLE "userpermission" DROP CONSTRAINT if exists "FK_users_userpermission";

ALTER TABLE "userpermission" ADD CONSTRAINT "FK_users_userpermission_added" FOREIGN KEY (up_added_by)
    references "users"(usid) CHECK;

ALTER TABLE "userpermission" ADD CONSTRAINT "FK_users_userpermission_lastupdated" FOREIGN KEY (up_lastupdated_by)
    references "users"(usid) CHECK;

ALTER TABLE "userpermission" ADD CONSTRAINT "FK_users_userpermission" FOREIGN KEY (up_usid)
    references "users"(usid) CHECK;

