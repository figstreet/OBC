
drop trigger if exists vendorcontact_insert;
drop trigger if exists vendorcontact_update;

create trigger vendorcontact_insert
    before insert
               on "vendorcontact"
                  for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

create trigger vendorcontact_update
    before update
               on "vendorcontact"
               for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

