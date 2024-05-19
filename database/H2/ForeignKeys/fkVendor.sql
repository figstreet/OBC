
ALTER TABLE "vendor" DROP CONSTRAINT if exists "FK_users_vendor_added";
ALTER TABLE "vendor" DROP CONSTRAINT if exists "FK_users_vendor_lastupdated";

ALTER TABLE "vendor" ADD CONSTRAINT "FK_users_vendor_added" FOREIGN KEY (vd_added_by)
    references "users"(usid) CHECK;

ALTER TABLE "vendor" ADD CONSTRAINT "FK_users_vendor_lastupdated" FOREIGN KEY (vd_lastupdated_by)
    references "users"(usid) CHECK;

