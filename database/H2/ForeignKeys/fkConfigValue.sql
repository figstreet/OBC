
ALTER TABLE "configvalue" DROP CONSTRAINT if exists "FK_users_configvalue_added";
ALTER TABLE "configvalue" DROP CONSTRAINT if exists "FK_users_configvalue_lastupdated";
ALTER TABLE "configvalue" DROP CONSTRAINT if exists "FK_client_configvalue";

ALTER TABLE "configvalue" ADD CONSTRAINT "FK_users_configvalue_added" FOREIGN KEY (cv_added_by)
    references "users"(usid) CHECK;

ALTER TABLE "configvalue" ADD CONSTRAINT "FK_users_configvalue_lastupdated" FOREIGN KEY (cv_lastupdated_by)
    references "users"(usid) CHECK;

ALTER TABLE "configvalue" ADD CONSTRAINT "FK_client_configvalue" FOREIGN KEY (cv_clid)
    references "client"(clid) CHECK;

