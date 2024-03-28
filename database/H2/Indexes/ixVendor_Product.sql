
DROP INDEX if exists "IX_vendor_product_added";
DROP INDEX if exists "IX_vendor_product_lastupdated";
DROP INDEX if exists "IX_vendor_product_prid";
DROP INDEX if exists "IX_vendor_product_vdid";

CREATE INDEX "IX_vendor_product_added"
    ON "vendor_product" ( vp_added_by );

CREATE INDEX "IX_vendor_product_lastupdated"
    ON "vendor_product" ( vp_lastupdated_by );

CREATE INDEX "IX_vendor_product_prid"
    ON "vendor_product" ( vp_prid );

CREATE INDEX "IX_vendor_product_vdid"
    ON "vendor_product" ( vp_vdid );

