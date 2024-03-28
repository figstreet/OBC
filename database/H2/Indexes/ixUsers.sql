
DROP INDEX if exists "IX_users_added";
DROP INDEX if exists "IX_users_lastupdated";

CREATE INDEX "IX_users_added"
    ON "users" ( us_added_by );

CREATE INDEX "IX_users_lastupdated"
    ON "users" ( us_lastupdated_by );

