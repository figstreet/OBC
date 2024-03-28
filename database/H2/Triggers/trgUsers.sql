
drop trigger if exists users_insert;
drop trigger if exists users_update;

create trigger users_insert
    before insert
               on "users"
                  for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

create trigger users_update
    before update
               on "users"
               for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

