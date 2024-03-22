
ALTER TABLE [dbo].[vendor_product]  WITH CHECK ADD
CONSTRAINT [FK_users_vendor_product_added_by] FOREIGN KEY([vp_added_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[vendor_product]  WITH CHECK ADD
CONSTRAINT [FK_users_vendor_product_lastupdated_by] FOREIGN KEY([vp_lastupdated_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[vendor_product]  WITH CHECK ADD
CONSTRAINT [FK_vendor_product_product] FOREIGN KEY([vp_prid])
REFERENCES [dbo].[product] ([prid]);

ALTER TABLE [dbo].[vendor_product]  WITH CHECK ADD
CONSTRAINT [FK_vendor_product_vendor] FOREIGN KEY([vp_vdid])
REFERENCES [dbo].[vendor] ([vdid]);
