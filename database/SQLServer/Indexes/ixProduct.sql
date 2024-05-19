
CREATE NONCLUSTERED INDEX [IX_product_added_by]
ON [dbo].[product] ( [pr_added_by] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;
