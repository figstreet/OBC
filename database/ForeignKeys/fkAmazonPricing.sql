
ALTER TABLE [dbo].[amazonpricing]  WITH CHECK ADD
CONSTRAINT [FK_users_amazonpricing_added_by] FOREIGN KEY([azp_added_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[amazonpricing]  WITH CHECK ADD
CONSTRAINT [FK_users_amazonpricing_lastupdated_by] FOREIGN KEY([azp_lastupdated_by])
REFERENCES [dbo].[users] ([usid]);

ALTER TABLE [dbo].[amazonpricing]  WITH CHECK ADD
CONSTRAINT [FK_vendor_product_amazonpricing] FOREIGN KEY([azp_vpid])
REFERENCES [dbo].[vendor_product] ([vpid]);