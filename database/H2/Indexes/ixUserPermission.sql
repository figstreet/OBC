
DROP INDEX if exists "IX_userpermission_added";
DROP INDEX if exists "IX_userpermission_lastupdated";
DROP INDEX if exists "IX_userpermission_usid";

CREATE INDEX "IX_userpermission_added"
    ON "userpermission" ( up_added_by );

CREATE INDEX "IX_userpermission_lastupdated"
    ON "userpermission" ( up_lastupdated_by );

CREATE INDEX "IX_userpermission_usid"
    ON "userpermission" ( up_usid );

