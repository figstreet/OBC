
CREATE NONCLUSTERED INDEX [IX_productrating_added_by]
ON [dbo].[productrating] ( [prr_added_by] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;
