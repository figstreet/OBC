
DROP INDEX if exists "IX_client_added";
DROP INDEX if exists "IX_client_lastupdated";

CREATE INDEX "IX_client_added"
    ON "client" ( cl_added_by );

CREATE INDEX "IX_client_lastupdated"
    ON "client" ( cl_lastupdated_by );

