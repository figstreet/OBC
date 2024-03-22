
ALTER TABLE [dbo].[amazonsalesrank]  WITH CHECK ADD
CONSTRAINT [FK_users_amazonsalesrank_added_by] FOREIGN KEY([azsr_added_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[amazonsalesrank]  WITH CHECK ADD
CONSTRAINT [FK_users_amazonsalesrank_lastupdated_by] FOREIGN KEY([azsr_lastupdated_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[amazonsalesrank]  WITH CHECK ADD
CONSTRAINT [FK_vendor_product_amazonsalesrank] FOREIGN KEY([azsr_vpid])
REFERENCES [dbo].[vendor_product] ([vpid]);