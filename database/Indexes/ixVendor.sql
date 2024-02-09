
CREATE NONCLUSTERED INDEX [IX_vendor_added_by]
ON [dbo].[vendor] ( [vd_added_by] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;
