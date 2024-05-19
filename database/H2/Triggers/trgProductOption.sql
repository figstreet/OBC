
drop trigger if exists productoption_insert;
drop trigger if exists productoption_update;

create trigger productoption_insert
    before insert
               on "productoption"
                  for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

create trigger productoption_update
    before update
               on "productoption"
               for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

