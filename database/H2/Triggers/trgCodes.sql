
drop trigger if exists codes_insert;
drop trigger if exists codes_update;

create trigger codes_insert
    before insert
               on "codes"
                  for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

create trigger codes_update
    before update
               on "codes"
               for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

