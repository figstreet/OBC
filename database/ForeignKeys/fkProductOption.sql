
ALTER TABLE [dbo].[productoption]  WITH CHECK ADD
CONSTRAINT [FK_product_productoption] FOREIGN KEY([pro_prid])
REFERENCES [dbo].[product] ([prid]);

ALTER TABLE [dbo].[productoption]  WITH CHECK ADD
CONSTRAINT [FK_users_productoption_added_by] FOREIGN KEY([pro_added_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[productoption]  WITH CHECK ADD
CONSTRAINT [FK_users_productoption_lastupdate_by] FOREIGN KEY([pro_lastupdated_by])
REFERENCES [dbo].[users] ([usid]);
