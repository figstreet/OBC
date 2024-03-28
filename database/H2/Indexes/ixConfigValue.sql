
DROP INDEX if exists "IX_configvalue_added";
DROP INDEX if exists "IX_configvalue_lastupdated";
DROP INDEX if exists "IX_configvalue_clid";

CREATE INDEX "IX_configvalue_added"
    ON "configvalue" ( cv_added_by );

CREATE INDEX "IX_configvalue_lastupdated"
    ON "configvalue" ( cv_lastupdated_by );

CREATE INDEX "IX_configvalue_clid"
    ON "configvalue" ( cv_clid );

