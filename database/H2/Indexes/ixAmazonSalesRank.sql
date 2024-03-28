

DROP INDEX if exists "IX_amazonsalesrank_added";
DROP INDEX if exists "IX_amazonsalesrank_lastupdated";
DROP INDEX if exists "IX_amazonsalesrank_vpid";

CREATE INDEX "IX_amazonsalesrank_added"
    ON "amazonsalesrank" ( azsr_added_by );

CREATE INDEX "IX_amazonsalesrank_lastupdated"
   ON "amazonsalesrank" ( azsr_lastupdated_by );

CREATE INDEX "IX_amazonsalesrank_vpid"
    ON "amazonsalesrank" ( azsr_vpid );
