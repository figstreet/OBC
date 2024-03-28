
DROP INDEX if exists "IX_history_added";
DROP INDEX if exists "IX_history_prid";
DROP INDEX if exists "IX_history_vdid";
DROP INDEX if exists "IX_history_vdaid";
DROP INDEX if exists "IX_history_vdcid";

CREATE INDEX "IX_history_added"
    ON "history" ( hi_added_by );

CREATE INDEX "IX_history_prid"
    ON "history" ( hi_prid );

CREATE INDEX "IX_history_vdid"
    ON "history" ( hi_vdid );

CREATE INDEX "IX_history_vdaid"
    ON "history" ( hi_vdaid );

CREATE INDEX "IX_history_vdcid"
    ON "history" ( hi_vdcid );

