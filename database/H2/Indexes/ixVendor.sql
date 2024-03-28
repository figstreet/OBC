
DROP INDEX if exists "IX_vendor_added";
DROP INDEX if exists "IX_vendor_lastupdated";

CREATE INDEX "IX_vendor_added"
    ON "vendor" ( vd_added_by );

CREATE INDEX "IX_vendor_lastupdated"
    ON "vendor" ( vd_lastupdated_by );

