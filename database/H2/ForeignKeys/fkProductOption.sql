

ALTER TABLE "productoption" DROP CONSTRAINT if exists "FK_users_productoption_added";
ALTER TABLE "productoption" DROP CONSTRAINT if exists "FK_users_productoption_lastupdated";
ALTER TABLE "productoption" DROP CONSTRAINT if exists "FK_product_productoption";

ALTER TABLE "productoption" ADD CONSTRAINT "FK_users_productoption_added" FOREIGN KEY (pro_added_by)
    references "users"(usid) CHECK;

ALTER TABLE "productoption" ADD CONSTRAINT "FK_users_productoption_lastupdated" FOREIGN KEY (pro_lastupdated_by)
    references "users"(usid) CHECK;

ALTER TABLE "productoption" ADD CONSTRAINT "FK_product_productoption" FOREIGN KEY (pro_prid)
    references "product"(prid) CHECK;

