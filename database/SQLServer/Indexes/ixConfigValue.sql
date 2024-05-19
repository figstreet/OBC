
CREATE NONCLUSTERED INDEX [IX_configvalue_added_by]
ON [dbo].[configvalue] ( [cv_added_by] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;
