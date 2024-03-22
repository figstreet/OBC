
GO

CREATE TRIGGER [dbo].[trgProductOptionInsertUpdate] ON [dbo].[productoption]
AFTER INSERT, UPDATE
AS
BEGIN
	if @@ROWCOUNT = 0 return
	SET NOCOUNT ON;
	
	update productoption set pro_added_dt = GETDATE()
	from productoption inner join INSERTED AS I
		on productoption.proid = I.proid
	left join DELETED as D
		on productoption.proid = D.proid
	where productoption.pro_added_dt is null and D.proid is null;
	
	update productoption set pro_lastupdated_dt = GETDATE()
	from productoption inner join INSERTED AS I
		on productoption.proid = I.proid;
END