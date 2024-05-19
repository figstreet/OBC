
CREATE NONCLUSTERED INDEX [IX_users_added_by]
ON [dbo].[users] ( [us_added_by] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;
