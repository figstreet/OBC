
CREATE NONCLUSTERED INDEX [IX_vendorcontact_added_by]
ON [dbo].[vendorcontact] ( [vdc_added_by] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;
