
ALTER TABLE [dbo].[codes]  WITH CHECK ADD
CONSTRAINT [FK_users_codes_added_by] FOREIGN KEY([co_added_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[codes]  WITH CHECK ADD
CONSTRAINT [FK_users_codes_lastupdated_by] FOREIGN KEY([co_lastupdated_by])
REFERENCES [dbo].[users] ([usid]);

