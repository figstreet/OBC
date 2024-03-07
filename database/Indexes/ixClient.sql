
CREATE NONCLUSTERED INDEX [IX_client_added_by]
ON [dbo].[client] ( [cl_added_by] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;
