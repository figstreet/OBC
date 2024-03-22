
ALTER TABLE "history" DROP CONSTRAINT if exists "FK_users_history_added";
ALTER TABLE "history" DROP CONSTRAINT if exists "FK_product_history";
ALTER TABLE "history" DROP CONSTRAINT if exists "FK_vendor_history";
ALTER TABLE "history" DROP CONSTRAINT if exists "FK_vendoraddress_history";
ALTER TABLE "history" DROP CONSTRAINT if exists "FK_vendorcontact_history";

ALTER TABLE "history" ADD CONSTRAINT "FK_users_history_added" FOREIGN KEY (hi_added_by)
    references "users"(usid) CHECK;

ALTER TABLE "history" ADD CONSTRAINT "FK_product_history" FOREIGN KEY (hi_prid)
    references "product"(prid) CHECK;

ALTER TABLE "history" ADD CONSTRAINT "FK_vendor_history" FOREIGN KEY (hi_vdid)
    references "vendor"(vdid) CHECK;

ALTER TABLE "history" ADD CONSTRAINT "FK_vendoraddress_history" FOREIGN KEY (hi_vdaid)
    references "vendoraddress"(vdaid) CHECK;

ALTER TABLE "history" ADD CONSTRAINT "FK_vendorcontact_history" FOREIGN KEY (hi_vdcid)
    references "vendorcontact"(vdcid) CHECK;

