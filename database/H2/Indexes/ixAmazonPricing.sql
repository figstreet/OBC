
DROP INDEX if exists "IX_amazonpricing_added";
DROP INDEX if exists "IX_amazonpricing_lastupdated";
DROP INDEX if exists "IX_amazonpricing_vpid";

CREATE INDEX "IX_amazonpricing_added"
    ON "amazonpricing" ( azp_added_by );

CREATE INDEX "IX_amazonpricing_lastupdated"
    ON "amazonpricing" ( azp_lastupdated_by );

CREATE INDEX "IX_amazonpricing_vpid"
    ON "amazonpricing" ( azp_vpid );

