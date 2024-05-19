
CREATE NONCLUSTERED INDEX [IX_history_prid]
ON [dbo].[history] ( [hi_prid] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;

CREATE NONCLUSTERED INDEX [IX_history_vdid]
ON [dbo].[history] ( [hi_vdid] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;

CREATE NONCLUSTERED INDEX [IX_history_vdaid]
ON [dbo].[history] ( [hi_vdaid] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;

CREATE NONCLUSTERED INDEX [IX_history_vdcid]
ON [dbo].[history] ( [hi_vdcid] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;

CREATE NONCLUSTERED INDEX [IX_history_added_by]
ON [dbo].[history] ( [hi_added_by] ASC )
WITH (PAD_INDEX = ON, FILLFACTOR = 90)
ON INDEX_FG;
