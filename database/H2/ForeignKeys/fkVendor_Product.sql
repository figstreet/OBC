
ALTER TABLE "vendor_product" DROP CONSTRAINT if exists "FK_users_vendor_product_added";
ALTER TABLE "vendor_product" DROP CONSTRAINT if exists "FK_users_vendor_product_lastupdated";
ALTER TABLE "vendor_product" DROP CONSTRAINT if exists "FK_product_vendor_product";
ALTER TABLE "vendor_product" DROP CONSTRAINT if exists "FK_vendor_vendor_product";

ALTER TABLE "vendor_product" ADD CONSTRAINT "FK_users_vendor_product_added" FOREIGN KEY (vp_added_by)
    references "users"(usid) CHECK;

ALTER TABLE "vendor_product" ADD CONSTRAINT "FK_users_vendor_product_lastupdated" FOREIGN KEY (vp_lastupdated_by)
    references "users"(usid) CHECK;

ALTER TABLE "vendor_product" ADD CONSTRAINT "FK_product_vendor_product" FOREIGN KEY (vp_prid)
    references "product"(prid) CHECK;

ALTER TABLE "vendor_product" ADD CONSTRAINT "FK_vendor_vendor_product" FOREIGN KEY (vp_vdid)
    references "vendor"(vdid) CHECK;

