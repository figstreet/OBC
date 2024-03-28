
drop trigger if exists amazonpricing_insert;
drop trigger if exists amazonpricing_update;

create trigger amazonpricing_insert
    before insert
               on "amazonpricing"
                  for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

create trigger amazonpricing_update
    before update
               on "amazonpricing"
               for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

