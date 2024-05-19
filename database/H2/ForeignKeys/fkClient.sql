
ALTER TABLE "client" DROP CONSTRAINT if exists "FK_users_client_added";
ALTER TABLE "client" DROP CONSTRAINT if exists "FK_users_client_lastupdated";

ALTER TABLE "client" ADD CONSTRAINT "FK_users_client_added" FOREIGN KEY (cl_added_by)
    references "users"(usid) CHECK;

ALTER TABLE "client" ADD CONSTRAINT "FK_users_client_lastupdated" FOREIGN KEY (cl_lastupdated_by)
    references "users"(usid) CHECK;

