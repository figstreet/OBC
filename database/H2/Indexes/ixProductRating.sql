
DROP INDEX if exists "IX_productrating_added";
DROP INDEX if exists "IX_productrating_lastupdated";
DROP INDEX if exists "IX_productrating_prid";

CREATE INDEX "IX_productrating_added"
    ON "productrating" ( prr_added_by );

CREATE INDEX "IX_productrating_lastupdated"
    ON "productrating" ( prr_lastupdated_by );

CREATE INDEX "IX_productrating_prid"
    ON "productrating" ( prr_prid );

