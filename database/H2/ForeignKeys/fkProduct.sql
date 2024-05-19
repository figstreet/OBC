
ALTER TABLE "product" DROP CONSTRAINT if exists "FK_users_product_added";
ALTER TABLE "product" DROP CONSTRAINT if exists "FK_users_product_lastupdated";

ALTER TABLE "product" ADD CONSTRAINT "FK_users_product_added" FOREIGN KEY (pr_added_by)
    references "users"(usid) CHECK;

ALTER TABLE "product" ADD CONSTRAINT "FK_users_product_lastupdated" FOREIGN KEY (pr_lastupdated_by)
    references "users"(usid) CHECK;

