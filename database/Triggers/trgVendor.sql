
CREATE OR ALTER TRIGGER [dbo].[trgVendorInsertUpdate] ON [dbo].[vendor] AFTER INSERT, UPDATE
AS
BEGIN
	if @@ROWCOUNT = 0 return;
	SET NOCOUNT ON;

	update vendor set vd_added_dt = GETDATE()
	from vendor inner join INSERTED AS I
		on vendor.vdid = I.vdid
	left join DELETED as D
		on vendor.vdid = D.vdid
	where vendor.vd_added_dt is null and D.vdid is null;

	update vendor set vd_lastupdated_dt = GETDATE()
	from vendor inner join INSERTED AS I
		on vendor.vdid = I.vdid;
END
