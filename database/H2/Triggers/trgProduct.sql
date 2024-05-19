
drop trigger if exists product_insert;
drop trigger if exists product_update;

create trigger product_insert
    before insert
               on "product"
                  for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

create trigger product_update
    before update
               on "product"
               for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

