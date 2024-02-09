
ALTER TABLE [dbo].[vendorcontact]  WITH CHECK ADD
CONSTRAINT [FK_users_vendorcontact_added_by] FOREIGN KEY([vdc_added_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[vendorcontact]  WITH CHECK ADD
CONSTRAINT [FK_users_vendorcontact_lastupdated_by] FOREIGN KEY([vdc_lastupdated_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[vendorcontact]  WITH CHECK ADD
CONSTRAINT [FK_vendor_vendorcontact] FOREIGN KEY([vdc_vdid])
REFERENCES [dbo].[vendor] ([vdid]);
