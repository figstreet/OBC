
drop trigger if exists vendor_insert;
drop trigger if exists vendor_update;

create trigger vendor_insert
    before insert
               on "vendor"
                  for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

create trigger vendor_update
    before update
               on "vendor"
               for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

