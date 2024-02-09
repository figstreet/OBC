
ALTER TABLE [dbo].[history]  WITH CHECK ADD
CONSTRAINT [FK_product_history] FOREIGN KEY([hi_prid])
REFERENCES [dbo].[product] ([prid]);

ALTER TABLE [dbo].[history]  WITH CHECK ADD
CONSTRAINT [FK_vendor_history] FOREIGN KEY([hi_vdid])
REFERENCES [dbo].[vendor] ([vdid]);

ALTER TABLE [dbo].[history]  WITH CHECK ADD
CONSTRAINT [FK_vendoraddress_history] FOREIGN KEY([hi_vdaid])
REFERENCES [dbo].[vendoraddress] ([vdaid]);

ALTER TABLE [dbo].[history]  WITH CHECK ADD
CONSTRAINT [FK_vendorcontact_history] FOREIGN KEY([hi_vdcid])
REFERENCES [dbo].[vendorcontact] ([vdcid]);

ALTER TABLE [dbo].[history]  WITH CHECK ADD
CONSTRAINT [FK_users_history_added_by] FOREIGN KEY([hi_added_by])
REFERENCES [dbo].[users] ([usid]);
