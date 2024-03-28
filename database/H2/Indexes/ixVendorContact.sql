
DROP INDEX if exists "IX_vendorcontact_added";
DROP INDEX if exists "IX_vendorcontact_lastupdated";
DROP INDEX if exists "IX_vendorcontact_vdid";

CREATE INDEX "IX_vendorcontact_added"
    ON "vendorcontact" ( vdc_added_by );

CREATE INDEX "IX_vendorcontact_lastupdated"
    ON "vendorcontact" ( vdc_lastupdated_by );

CREATE INDEX "IX_vendorcontact_vdid"
    ON "vendorcontact" ( vdc_vdid );

