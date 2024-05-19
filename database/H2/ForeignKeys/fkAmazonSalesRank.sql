
ALTER TABLE "amazonsalesrank" DROP CONSTRAINT if exists "FK_users_amazonsalesrank_added";
ALTER TABLE "amazonsalesrank" DROP CONSTRAINT if exists "FK_users_amazonsalesrank_lastupdated";
ALTER TABLE "amazonsalesrank" DROP CONSTRAINT if exists "FK_vendor_product_amazonsalesrank";

ALTER TABLE "amazonsalesrank" ADD CONSTRAINT "FK_users_amazonsalesrank_added" FOREIGN KEY (azsr_added_by)
    references "users"(usid) CHECK;

ALTER TABLE "amazonsalesrank" ADD CONSTRAINT "FK_users_amazonsalesrank_lastupdated" FOREIGN KEY (azsr_lastupdated_by)
    references "users"(usid) CHECK;

ALTER TABLE "amazonsalesrank" ADD CONSTRAINT "FK_vendor_product_amazonsalesrank" FOREIGN KEY (azsr_vpid)
    references "vendor_product"(vpid) CHECK;

