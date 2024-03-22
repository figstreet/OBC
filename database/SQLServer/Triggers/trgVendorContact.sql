
GO

CREATE TRIGGER [dbo].[trgVendorContactInsertUpdate] ON [dbo].[vendorcontact] AFTER INSERT, UPDATE
AS
BEGIN
if @@ROWCOUNT = 0 return
SET NOCOUNT ON;

update vendorcontact set vdc_added_dt = GETDATE()
from vendorcontact inner join INSERTED AS I
	on vendorcontact.vdcid = I.vdcid
left join DELETED as D
	on vendorcontact.vdcid = D.vdcid
where vendorcontact.vdc_added_dt is null and D.vdcid is null;

update vendorcontact set vdc_lastupdated_dt = GETDATE()
from vendorcontact inner join INSERTED AS I
	on vendorcontact.vdcid = I.vdcid
END