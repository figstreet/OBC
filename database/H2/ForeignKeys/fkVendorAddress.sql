
ALTER TABLE [dbo].[vendoraddress]  WITH CHECK ADD
CONSTRAINT [FK_users_vendoraddress_added_by] FOREIGN KEY([vda_added_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[vendoraddress]  WITH CHECK ADD
CONSTRAINT [FK_users_vendoraddress_lastupdate_by] FOREIGN KEY([vda_lastupdated_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[vendoraddress]  WITH CHECK ADD
CONSTRAINT [FK_vendor_vendoraddress] FOREIGN KEY([vda_vdid])
REFERENCES [dbo].[vendor] ([vdid]);
