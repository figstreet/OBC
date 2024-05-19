
DROP INDEX if exists "IX_vendoraddress_added";
DROP INDEX if exists "IX_vendoraddress_lastupdated";
DROP INDEX if exists "IX_vendoraddress_vdid";

CREATE INDEX "IX_vendoraddress_added"
    ON "vendoraddress" ( vda_added_by );

CREATE INDEX "IX_vendoraddress_lastupdated"
    ON "vendoraddress" ( vda_lastupdated_by );

CREATE INDEX "IX_vendoraddress_vdid"
    ON "vendoraddress" ( vda_vdid );

