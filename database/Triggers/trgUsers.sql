
CREATE OR ALTER TRIGGER [dbo].[trgUsersInsertUpdate] ON [dbo].[users]
AFTER INSERT, UPDATE
AS
BEGIN
	if @@ROWCOUNT = 0 return
	SET NOCOUNT ON;

	update users set us_added_dt = GETDATE()
	from users inner join INSERTED AS I
		on users.usid = I.usid
	left join DELETED as D
		on users.usid = D.usid
	where users.us_added_dt is null and D.usid is null;
	
	update users set us_lastupdated_dt = GETDATE()
	from users inner join INSERTED AS I
		on users.usid = I.usid
END