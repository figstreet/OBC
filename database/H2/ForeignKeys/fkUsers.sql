
ALTER TABLE "users" DROP CONSTRAINT if exists "FK_users_users_added";
ALTER TABLE "users" DROP CONSTRAINT if exists "FK_users_users_lastupdated";

ALTER TABLE "users" ADD CONSTRAINT "FK_users_users_added" FOREIGN KEY (us_added_by)
    references "users"(usid) CHECK;

ALTER TABLE "users" ADD CONSTRAINT "FK_users_users_lastupdated" FOREIGN KEY (us_lastupdated_by)
    references "users"(usid) CHECK;

