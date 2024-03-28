
ALTER TABLE "amazonpricing" DROP CONSTRAINT if exists "FK_users_amazonpricing_added";
ALTER TABLE "amazonpricing" DROP CONSTRAINT if exists "FK_users_amazonpricing_lastupdated";
ALTER TABLE "amazonpricing" DROP CONSTRAINT if exists "FK_vendor_product_amazonpricing";

ALTER TABLE "amazonpricing" ADD CONSTRAINT "FK_users_amazonpricing_added" FOREIGN KEY (azp_added_by)
    references "users"(usid) CHECK;

ALTER TABLE "amazonpricing" ADD CONSTRAINT "FK_users_amazonpricing_lastupdated" FOREIGN KEY (azp_lastupdated_by)
    references "users"(usid) CHECK;

ALTER TABLE "amazonpricing" ADD CONSTRAINT "FK_vendor_product_amazonpricing" FOREIGN KEY (azp_vpid)
    references "vendor_product"(vpid) CHECK;

