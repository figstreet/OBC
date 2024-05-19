
ALTER TABLE "vendoraddress" DROP CONSTRAINT if exists "FK_users_vendoraddress_added";
ALTER TABLE "vendoraddress" DROP CONSTRAINT if exists "FK_users_vendoraddress_lastupdated";
ALTER TABLE "vendoraddress" DROP CONSTRAINT if exists "FK_vendor_vendoraddress";

ALTER TABLE "vendoraddress" ADD CONSTRAINT "FK_users_vendoraddress_added" FOREIGN KEY (vda_added_by)
    references "users"(usid) CHECK;

ALTER TABLE "vendoraddress" ADD CONSTRAINT "FK_users_vendoraddress_lastupdated" FOREIGN KEY (vda_lastupdated_by)
    references "users"(usid) CHECK;

ALTER TABLE "vendoraddress" ADD CONSTRAINT "FK_vendor_vendoraddress" FOREIGN KEY (vda_vdid)
    references "vendor"(vdid) CHECK;

