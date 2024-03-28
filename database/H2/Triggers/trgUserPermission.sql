
drop trigger if exists userpermission_insert;
drop trigger if exists userpermission_update;

create trigger userpermission_insert
    before insert
               on "userpermission"
                  for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

create trigger userpermission_update
    before update
               on "userpermission"
               for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

