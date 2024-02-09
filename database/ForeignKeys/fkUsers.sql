
ALTER TABLE [dbo].[users]  WITH CHECK ADD
CONSTRAINT [FK_users_added_by] FOREIGN KEY([us_added_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[users]  WITH CHECK ADD
CONSTRAINT [FK_users_lastupdate_by] FOREIGN KEY([us_lastupdated_by])
REFERENCES [dbo].[users] ([usid]);
