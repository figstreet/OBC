
ALTER TABLE "codes" DROP CONSTRAINT if exists "FK_users_codes_added";
ALTER TABLE "codes" DROP CONSTRAINT if exists "FK_users_codes_lastupdated";

ALTER TABLE "codes" ADD CONSTRAINT "FK_users_codes_added" FOREIGN KEY (co_added_by)
    references "users"(usid) CHECK;

ALTER TABLE "codes" ADD CONSTRAINT "FK_users_codes_lastupdated" FOREIGN KEY (co_lastupdated_by)
    references "users"(usid) CHECK;

