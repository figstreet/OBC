
CREATE OR ALTER TRIGGER [dbo].[trgUserPermissionInsertUpdate] ON [dbo].[userpermission] AFTER INSERT, UPDATE
AS
BEGIN
if @@ROWCOUNT = 0 return
SET NOCOUNT ON;

update userpermission set up_added_dt = GETDATE()
from userpermission inner join INSERTED AS I
	on userpermission.upid = I.upid
left join DELETED as D
	on userpermission.upid = D.upid
where userpermission.up_added_dt is null and D.upid is null;

update userpermission set up_lastupdated_dt = GETDATE()
from userpermission inner join INSERTED AS I
	on userpermission.upid = I.upid
END