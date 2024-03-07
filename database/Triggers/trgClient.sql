
GO

CREATE TRIGGER [dbo].[trgClientInsertUpdate] ON [dbo].[client]
AFTER INSERT, UPDATE
AS
BEGIN
	if @@ROWCOUNT = 0 return
	SET NOCOUNT ON;
	
	update client set cl_added_dt = GETDATE()
	from client inner join INSERTED AS I
		on client.clid = I.clid
	left join DELETED as D
		on client.clid = D.clid
	where client.cl_added_dt is null and D.clid is null;

	update client set cl_lastupdated_dt = GETDATE()
	from client inner join INSERTED AS I
		on client.clid = I.clid
END