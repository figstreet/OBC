
ALTER TABLE "vendorcontact" DROP CONSTRAINT if exists "FK_users_vendorcontact_added";
ALTER TABLE "vendorcontact" DROP CONSTRAINT if exists "FK_users_vendorcontact_lastupdated";
ALTER TABLE "vendorcontact" DROP CONSTRAINT if exists "FK_vendor_vendorcontact";

ALTER TABLE "vendorcontact" ADD CONSTRAINT "FK_users_vendorcontact_added" FOREIGN KEY (vdc_added_by)
    references "users"(usid) CHECK;

ALTER TABLE "vendorcontact" ADD CONSTRAINT "FK_users_vendorcontact_lastupdated" FOREIGN KEY (vdc_lastupdated_by)
    references "users"(usid) CHECK;

ALTER TABLE "vendorcontact" ADD CONSTRAINT "FK_vendor_vendorcontact" FOREIGN KEY (vdc_vdid)
    references "vendor"(vdid) CHECK;

