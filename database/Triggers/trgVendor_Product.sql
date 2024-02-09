
CREATE OR ALTER TRIGGER [dbo].[trgVendor_ProductInsertUpdate] ON [dbo].[vendor_product] AFTER INSERT, UPDATE
AS
BEGIN
if @@ROWCOUNT = 0 return
SET NOCOUNT ON;

update vendor_product set vp_added_dt = GETDATE()
	from vendor_product inner join INSERTED AS I
		on vendor_product.vpid = I.vpid
	left join DELETED as D
		on vendor_product.vpid = D.vpid
	where vendor_product.vp_added_dt is null and D.vpid is null;

update vendor_product set vp_lastupdated_dt = GETDATE()
from vendor_product inner join INSERTED AS I
	on vendor_product.vpid = I.vpid
END
