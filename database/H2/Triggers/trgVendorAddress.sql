
drop trigger if exists vendoraddress_insert;
drop trigger if exists vendoraddress_update;

create trigger vendoraddress_insert
    before insert
               on "vendoraddress"
                  for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

create trigger vendoraddress_update
    before update
               on "vendoraddress"
               for each row call "com.figstreet.core.h2server.databasetriggers.LastupdateTrigger";

