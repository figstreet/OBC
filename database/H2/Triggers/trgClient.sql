
drop trigger if exists client_insert;
drop trigger if exists client_update;

create trigger client_insert
    before insert
               on "client"
                  for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

create trigger client_update
    before update
               on "client"
               for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

