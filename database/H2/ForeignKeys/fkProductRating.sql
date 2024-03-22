
ALTER TABLE [dbo].[productrating]  WITH CHECK ADD
CONSTRAINT [FK_users_productrating_added_by] FOREIGN KEY([prr_added_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[productrating]  WITH CHECK ADD
CONSTRAINT [FK_users_productrating_lastupdated_by] FOREIGN KEY([prr_lastupdated_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[productrating]  WITH CHECK ADD
CONSTRAINT [FK_product_productrating] FOREIGN KEY([prr_prid])
REFERENCES [dbo].[product] ([prid]);

ALTER TABLE [dbo].[productrating]  WITH CHECK ADD
CONSTRAINT [FK_users_productrating] FOREIGN KEY([prr_usid])
REFERENCES [dbo].[users] ([usid]);
