
CREATE OR ALTER TRIGGER [dbo].[trgHistoryInsertUpdate] ON [dbo].[history] AFTER INSERT, UPDATE
AS
BEGIN
if @@ROWCOUNT = 0 return
SET NOCOUNT ON;

update history set hi_added_dt = GETDATE()
from history inner join INSERTED AS I
	on history.hiid = I.hiid
left join DELETED as D
	on history.hiid = D.hiid
where history.hi_added_dt is null and D.hiid is null;

END