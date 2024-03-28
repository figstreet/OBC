
drop trigger if exists configvalue_insert;
drop trigger if exists configvalue_update;

create trigger configvalue_insert
    before insert
               on "configvalue"
                  for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

create trigger configvalue_update
    before update
               on "configvalue"
               for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

