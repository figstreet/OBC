
DROP INDEX if exists "IX_codes_added";
DROP INDEX if exists "IX_codes_lastupdated";

CREATE INDEX "IX_codes_added"
    ON "codes" ( co_added_by );

CREATE INDEX "IX_codes_lastupdated"
    ON "codes" ( co_lastupdated_by );

