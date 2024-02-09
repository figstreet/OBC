
CREATE NONCLUSTERED INDEX [IX_userpermission_added_by]
ON [dbo].[userpermission] ( [up_added_by] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;
