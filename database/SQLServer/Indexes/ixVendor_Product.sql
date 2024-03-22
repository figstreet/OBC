
CREATE NONCLUSTERED INDEX [IX_vendor_product_added_by]
ON [dbo].[vendor_product] ( [vp_added_by] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;
