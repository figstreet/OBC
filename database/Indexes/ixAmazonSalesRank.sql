
CREATE NONCLUSTERED INDEX [IX_amazonsalesrank_added_by]
ON [dbo].[amazonsalesrank] ( [azsr_added_by] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;

CREATE NONCLUSTERED INDEX [IX_amazonsalesrank_vpid]
ON [dbo].[amazonsalesrank] ( [azsr_vpid] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;

