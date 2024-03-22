
GO

CREATE TRIGGER [dbo].[trgProductInsertUpdate] ON [dbo].[product]
AFTER INSERT, UPDATE
AS
BEGIN
	if @@ROWCOUNT = 0 return
	SET NOCOUNT ON;
	
	update product set pr_added_dt = GETDATE()
	from product inner join INSERTED AS I
		on product.prid = I.prid
	left join DELETED as D
		on product.prid = D.prid
	where product.pr_added_dt is null and D.prid is null;

	update product set pr_lastupdated_dt = GETDATE()
	from product inner join INSERTED AS I
	on product.prid = I.prid
END