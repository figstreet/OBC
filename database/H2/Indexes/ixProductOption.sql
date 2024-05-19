
DROP INDEX if exists "IX_productoption_added";
DROP INDEX if exists "IX_productoption_lastupdated";
DROP INDEX if exists "IX_productoption_prid";

CREATE INDEX "IX_productoption_added"
    ON "productoption" ( pro_added_by );

CREATE INDEX "IX_productoption_lastupdated"
    ON "productoption" ( pro_lastupdated_by );

CREATE INDEX "IX_productoption_prid"
    ON "productoption" ( pro_prid );

