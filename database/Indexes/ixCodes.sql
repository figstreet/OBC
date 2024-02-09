
CREATE NONCLUSTERED INDEX [IX_codes_added_by]
ON [dbo].[codes] ( [co_added_by] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;
