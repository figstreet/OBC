
CREATE NONCLUSTERED INDEX [IX_vendoraddress_added_by]
ON [dbo].[vendoraddress] ( [vda_added_by] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;
