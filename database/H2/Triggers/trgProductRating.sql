
GO

CREATE TRIGGER [dbo].[trgProductRatingInsertUpdate] ON [dbo].[productrating]
AFTER INSERT, UPDATE
AS
BEGIN
	if @@ROWCOUNT = 0 return
	SET NOCOUNT ON;
	
	update productrating set prr_added_dt = GETDATE()
	from productrating inner join INSERTED AS I
		on productrating.prrid = I.prrid
	left join DELETED as D
		on productrating.prrid = D.prrid
	where productrating.prr_added_dt is null and D.prrid is null;
	

	update productrating set prr_lastupdated_dt = GETDATE()
	from productrating inner join INSERTED AS I
		on productrating.prrid = I.prrid;
END