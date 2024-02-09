
ALTER TABLE [dbo].[configvalue]  WITH CHECK ADD
CONSTRAINT [FK_users_configvalue_added_by] FOREIGN KEY([cv_added_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[configvalue]  WITH CHECK ADD
CONSTRAINT [FK_users_configvalue_lastupdated_by] FOREIGN KEY([cv_lastupdated_by])
REFERENCES [dbo].[users] ([usid]);


