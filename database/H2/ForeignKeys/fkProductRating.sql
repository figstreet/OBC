
ALTER TABLE "productrating" DROP CONSTRAINT if exists "FK_users_productrating_added";
ALTER TABLE "productrating" DROP CONSTRAINT if exists "FK_users_productrating_lastupdated";
ALTER TABLE "productrating" DROP CONSTRAINT if exists "FK_product_productrating";
ALTER TABLE "productrating" DROP CONSTRAINT if exists "FK_users_productrating";

ALTER TABLE "productrating" ADD CONSTRAINT "FK_users_productrating_added" FOREIGN KEY (prr_added_by)
    references "users"(usid) CHECK;

ALTER TABLE "productrating" ADD CONSTRAINT "FK_users_productrating_lastupdated" FOREIGN KEY (prr_lastupdated_by)
    references "users"(usid) CHECK;

ALTER TABLE "productrating" ADD CONSTRAINT "FK_product_productrating" FOREIGN KEY (prr_prid)
    references "product"(prid) CHECK;

ALTER TABLE "productrating" ADD CONSTRAINT "FK_users_productrating" FOREIGN KEY (prr_usid)
    references "users"(usid) CHECK;

