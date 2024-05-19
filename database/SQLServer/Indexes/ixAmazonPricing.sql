
CREATE NONCLUSTERED INDEX [IX_amazonpricing_added_by]
ON [dbo].[amazonpricing] ( [azp_added_by] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;

CREATE NONCLUSTERED INDEX [IX_amazonpricing_vpid]
ON [dbo].[amazonpricing] ( [azp_vpid] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;

