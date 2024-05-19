
ALTER TABLE [dbo].[client]  WITH CHECK ADD
CONSTRAINT [FK_users_client_added_by] FOREIGN KEY([cl_added_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[client]  WITH CHECK ADD
CONSTRAINT [FK_users_client_lastupdated_by] FOREIGN KEY([cl_lastupdated_by])
REFERENCES [dbo].[users] ([usid]);

