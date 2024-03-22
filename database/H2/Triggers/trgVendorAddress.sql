
GO

CREATE TRIGGER [dbo].[trgVendorAddressInsertUpdate] ON [dbo].[vendoraddress] AFTER INSERT, UPDATE
AS
BEGIN
if @@ROWCOUNT = 0 return
SET NOCOUNT ON;

update vendoraddress set vda_added_dt = GETDATE()
from vendoraddress inner join INSERTED AS I
	on vendoraddress.vdaid = I.vdaid
left join DELETED as D
	on vendoraddress.vdaid = D.vdaid
where vendoraddress.vda_added_dt is null and D.vdaid is null;

update vendoraddress set vda_lastupdated_dt = GETDATE()
from vendoraddress inner join INSERTED AS I
	on vendoraddress.vdaid = I.vdaid
END
