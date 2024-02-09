
ALTER TABLE [dbo].[vendor]  WITH CHECK ADD
CONSTRAINT [FK_users_vendor_added_by] FOREIGN KEY([vd_added_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[vendor]  WITH CHECK ADD
CONSTRAINT [FK_users_vendor_lastupdate_by] FOREIGN KEY([vd_lastupdated_by])
REFERENCES [dbo].[users] ([usid]);
