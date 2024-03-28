
DROP INDEX if exists "IX_product_added";
DROP INDEX if exists "IX_product_lastupdated";

CREATE INDEX "IX_product_added"
    ON "product" ( pr_added_by );

CREATE INDEX "IX_product_lastupdated"
    ON "product" ( pr_lastupdated_by );
