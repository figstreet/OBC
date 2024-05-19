
ALTER TABLE [dbo].[userpermission]  WITH CHECK ADD
CONSTRAINT [FK_users_userpermission_added_by] FOREIGN KEY([up_added_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[userpermission]  WITH CHECK ADD
CONSTRAINT [FK_users_userpermission_lastupdated_by] FOREIGN KEY([up_lastupdated_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[userpermission]  WITH CHECK ADD
CONSTRAINT [FK_users_userpermission] FOREIGN KEY([up_usid])
REFERENCES [dbo].[users] ([usid]);
