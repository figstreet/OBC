
CREATE OR ALTER TRIGGER [dbo].[trgAmazonPricingInsertUpdate] ON [dbo].[amazonpricing] 
AFTER INSERT, UPDATE
AS
BEGIN
if @@ROWCOUNT = 0 return
SET NOCOUNT ON;

update amazonpricing set azp_added_dt = GETDATE()
	from amazonpricing inner join INSERTED AS I
		on amazonpricing.azpid = I.azpid
	left join DELETED as D
		on amazonpricing.azpid = D.azpid
	where amazonpricing.azp_added_dt is null and D.azpid is null;

update amazonpricing set azp_lastupdated_dt = GETDATE()
from amazonpricing inner join INSERTED AS I
	on amazonpricing.azpid = I.azpid
END
