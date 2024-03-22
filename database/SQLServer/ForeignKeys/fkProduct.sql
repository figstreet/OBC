
ALTER TABLE [dbo].[product]  WITH CHECK ADD 
CONSTRAINT [FK_users_product_added_by] FOREIGN KEY([pr_added_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[product]  WITH CHECK ADD
CONSTRAINT [FK_users_product_lastupdated_by] FOREIGN KEY([pr_lastupdated_by])
REFERENCES [dbo].[users] ([usid]);
