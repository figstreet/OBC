
CREATE OR ALTER TRIGGER [dbo].[trgCodesInsertUpdate] ON [dbo].[codes]
AFTER INSERT, UPDATE
AS
BEGIN
	if @@ROWCOUNT = 0 return
	SET NOCOUNT ON;
	
	update codes set co_added_dt = GETDATE()
	from codes inner join INSERTED AS I
		on codes.coid = I.coid
	left join DELETED as D
		on codes.coid = D.coid
	where codes.co_added_dt is null and D.coid is null;

	update codes set co_lastupdated_dt = GETDATE()
	from codes inner join INSERTED AS I
		on codes.coid = I.coid
END