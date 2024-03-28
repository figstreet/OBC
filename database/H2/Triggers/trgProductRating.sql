
drop trigger if exists productrating_insert;
drop trigger if exists productrating_update;

create trigger productrating_insert
    before insert
               on "productrating"
                  for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

create trigger productrating_update
    before update
               on "productrating"
               for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

