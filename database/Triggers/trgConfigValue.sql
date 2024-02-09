
CREATE OR ALTER TRIGGER [dbo].[trgConfigValueInsertUpdate] ON [dbo].[configvalue] AFTER INSERT, UPDATE
AS
BEGIN
if @@ROWCOUNT = 0 return
SET NOCOUNT ON;

update configvalue set cv_added_dt = GETDATE()
from configvalue inner join INSERTED AS I
	on configvalue.cvid = I.cvid
left join DELETED as D
	on configvalue.cvid = D.cvid
where configvalue.cv_added_dt is null and D.cvid is null;

update configvalue set cv_lastupdated_dt = GETDATE()
from configvalue inner join INSERTED AS I
	on configvalue.cvid = I.cvid
END