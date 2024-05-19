
drop trigger if exists amazonsalesrank_insert;
drop trigger if exists amazonsalesrank_update;

create trigger amazonsalesrank_insert
    before insert
               on "amazonsalesrank"
                  for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

create trigger amazonsalesrank_update
    before update
               on "amazonsalesrank"
               for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

