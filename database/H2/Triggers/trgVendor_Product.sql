
drop trigger if exists vendor_product_insert;
drop trigger if exists vendor_product_update;

create trigger vendor_product_insert
    before insert
               on "vendor_product"
                  for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

create trigger vendor_product_update
    before update
               on "vendor_product"
               for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

