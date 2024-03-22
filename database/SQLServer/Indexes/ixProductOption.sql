
CREATE NONCLUSTERED INDEX [IX_productoption_added_by]
ON [dbo].[productoption] ( [pro_added_by] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;
