
GO

CREATE TRIGGER [dbo].[trgAmazonSalesRankInsertUpdate] ON [dbo].[amazonsalesrank]
AFTER INSERT, UPDATE
AS
BEGIN
if @@ROWCOUNT = 0 return
SET NOCOUNT ON;

update amazonsalesrank set azsr_added_dt = GETDATE()
	from amazonsalesrank inner join INSERTED AS I
		on amazonsalesrank.azsrid = I.azsrid
	left join DELETED as D
		on amazonsalesrank.azsrid = D.azsrid
	where amazonsalesrank.azsr_added_dt is null and D.azsrid is null;

update amazonsalesrank set azsr_lastupdated_dt = GETDATE()
from amazonsalesrank inner join INSERTED AS I
	on amazonsalesrank.azsrid = I.azsrid
END
