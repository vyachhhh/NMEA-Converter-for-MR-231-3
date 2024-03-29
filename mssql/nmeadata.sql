USE [master];
IF NOT EXISTS (SELECT Name from sys.databases where Name='NMEA')
BEGIN
    CREATE DATABASE [NMEA]
END
GO
    USE [NMEA];
GO

CREATE TABLE [dbo].[NmeaFormat](
	[FormatID] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[FormatName] [varchar](32) NULL
);
GO

CREATE TABLE [dbo].[NmeaMessage](
	[MessageID] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[MessageFormatID] [int] NULL,
	[MessageRecTime] [datetime] NULL,
	[MessageTime] [bigint] NULL,
	[Message] [varchar](128) NULL
);
GO
create view [dbo].[vNmeaMessage] as
(
select [MessageID],[FormatName],[MessageRecTime],[MessageTime],[Message]
from [dbo].[NmeaMessage] NM 
join [dbo].[NmeaFormat] NF on NM.MessageFormatID = Nf.FormatID
);
GO
CREATE TABLE [dbo].[TargetType](
	[TargetTypeID] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[TypeChar] [char](1) NULL,
	[TypeName] [varchar](32) NULL
);
GO
CREATE TABLE [dbo].[TargetStatus](
	[TargetStatusID] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[StatusChar] [char](1) NULL,
	[StatusName] [varchar](32) NULL
);
GO
CREATE TABLE [dbo].[Iff](
	[IffID] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[IffChar] [char](1) NULL,
	[IffName] [varchar](32) NULL
);
GO
CREATE TABLE [dbo].[CourseIndicator](
	[CourseIndicatorID] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[CourseIndicatorChar] [char](1) NULL,
	[CourseIndicatorName] [varchar](32) NULL
);
GO
CREATE TABLE [dbo].[Ttm](
	[MessageID] [int] NULL FOREIGN KEY REFERENCES [dbo].[NmeaMessage]([MessageID]),
	[TargetNumber] [int] NULL,
	[Distance] [float] NULL,
	[Bearing] [float] NULL,
	[Course] [float] NULL,
	[CourseIndicatorID] [int] NULL FOREIGN KEY REFERENCES [dbo].[CourseIndicator]([CourseIndicatorID]),
	[Speed] [float] NULL,
	[TargetTypeID] [int] NULL FOREIGN KEY REFERENCES [dbo].[TargetType]([TargetTypeID]),
	[TargetStatusID] [int] NULL FOREIGN KEY REFERENCES [dbo].[TargetStatus](TargetStatusID),
	[IffID] [int] NULL FOREIGN KEY REFERENCES [dbo].[Iff](IffId),
	[Interval] [bigint] NULL
);
GO
create function [dbo].[fGetMessageTTM]
(
	@id int
)
returns table as return
(
	select [FormatName],[TargetNumber],[Distance],
	[Bearing],[Course],[CourseIndicatorName],[Speed],
	[TypeName],[StatusName],[IffName],[Interval]
	from [dbo].[Ttm] TTM
	join [dbo].[NmeaMessage] NM on NM.[MessageID] = TTM.[MessageID]
	join [dbo].[NmeaFormat] NF on NF.[FormatID] = NM.[MessageFormatID]
	join [dbo].[CourseIndicator] CI on CI.[CourseIndicatorID] = TTM.[CourseIndicatorID]
	join [dbo].[TargetType] TT on TT.[TargetTypeID] = TTM.[TargetTypeID]
	join [dbo].[TargetStatus] TS on TS.[TargetStatusID] = TTM.[TargetStatusID]
	join [dbo].[Iff] IFF on IFF.[IffID] = TTM.[IffID]
	where TTM.MessageID = @id
);
GO
create function [dbo].[fGetFormatByMessageId]
(
	@id int
)
returns table as return
(
	select [FormatName] from [dbo].[NmeaFormat] NF
	join [dbo].[NmeaMessage] NM on NM.MessageFormatID = NF.FormatID
	where NM.MessageID = @id
);
GO
CREATE TABLE [dbo].[DistanceUnit](
	[DistanceUnitID] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[DistanceUnitChar] [char](1) NULL,
	[DistanceUnitName] [varchar](32) NULL
);
GO
CREATE TABLE [dbo].[DisplayOrientation](
	[DisplayOrientationID] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[DisplayOrientationChar] [char](1) NULL,
	[DisplayOrientationName] [varchar](32) NULL
);
GO
CREATE TABLE [dbo].[WorkingMode](
	[WorkingModeID] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[WorkingModeChar] [char](1) NULL,
	[WorkingModeName] [varchar](32) NULL
);
GO
CREATE TABLE [dbo].[Rsd](
	[MessageID] [int] NULL FOREIGN KEY REFERENCES [dbo].[NmeaMessage]([MessageID]),
	[InitialDistance] [float] NULL,
	[InitialBearing] [float] NULL,
	[MovingCircleOfDistance] [float] NULL,
	[Bearing] [float] NULL,
	[DistanceFromShip] [float] NULL,
	[Bearing2] [float] NULL,
	[DistanceScale] [float] NULL,
	[DistanceUnitID] [int] NULL FOREIGN KEY REFERENCES [dbo].[DistanceUnit](DistanceUnitID),
	[DisplayOrientationID] [int] NULL FOREIGN KEY REFERENCES [dbo].[DisplayOrientation]([DisplayOrientationID]),
	[WorkingModeID] [int] NULL FOREIGN KEY REFERENCES [dbo].[WorkingMode]([WorkingModeID])
);
GO
CREATE function [dbo].[fGetMessageRSD]
(
	@id int
)
returns table as return
(
	select [InitialDistance],[InitialBearing],[MovingCircleOfDistance],
	[Bearing],[DistanceFromShip],[Bearing2],[DistanceScale],
	DU.DistanceUnitChar,
	DO.DisplayOrientationChar,
	WM.WorkingModeChar
	from [dbo].[Rsd] RSD
	join [dbo].[DistanceUnit] DU on DU.DistanceUnitID = RSD.DistanceUnitID
	join DisplayOrientation DO on DO.DisplayOrientationID = RSD.DisplayOrientationID
	join WorkingMode WM on WM.WorkingModeID = RSD.WorkingModeID
	where RSD.MessageID = @id
);
GO
CREATE TABLE [dbo].[CourseAttr](
	[CourseAttrID] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[CourseAttrChar] [char](1) NULL,
	[CourseAttrName] [varchar](32) NULL
);
GO
CREATE TABLE [dbo].[SpeedUnit](
	[SpeedUnitID] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[SpeedUnitChar] [char](1) NULL,
	[SpeedUnitName] [varchar](32) NULL
);
GO
CREATE TABLE [dbo].[Vhw](
	[MessageID] [int] NULL FOREIGN KEY REFERENCES [dbo].[NmeaMessage]([MessageID]),
	[Course] [float] NULL,
	[CourseAttrID] [int] NULL FOREIGN KEY REFERENCES [dbo].[CourseAttr]([CourseAttrID]),
	[Speed] [float] NULL,
	[SpeedUnitID] [int] NULL FOREIGN KEY REFERENCES [dbo].[SpeedUnit]([SpeedUnitID])
);
GO
CREATE function [dbo].[fGetMessageVHW]
(
	@id int
)
returns table as return
(
	select [Course],
	CA.CourseAttrChar,
	[Speed],
	SU.SpeedUnitChar
	from Vhw VHW
	join CourseAttr CA on CA.CourseAttrID = VHW.CourseAttrID
	join SpeedUnit SU on SU.SpeedUnitID = VHW.SpeedUnitID
	where VHW.MessageID = @id
);
GO
create function [dbo].[fGetLastMessageId]
()
returns table as return
(
	select TOP(1) [MessageID] from [dbo].[NmeaMessage]
	order by [MessageID] desc
);
GO
SET IDENTITY_INSERT [dbo].[CourseAttr] ON 

INSERT [dbo].[CourseAttr] ([CourseAttrID], [CourseAttrChar], [CourseAttrName]) VALUES (1, N'T', NULL)
SET IDENTITY_INSERT [dbo].[CourseAttr] OFF
GO
SET IDENTITY_INSERT [dbo].[CourseIndicator] ON 

INSERT [dbo].[CourseIndicator] ([CourseIndicatorID], [CourseIndicatorChar], [CourseIndicatorName]) VALUES (1, N'T', N'True')
INSERT [dbo].[CourseIndicator] ([CourseIndicatorID], [CourseIndicatorChar], [CourseIndicatorName]) VALUES (2, N'R', N'Relative')
SET IDENTITY_INSERT [dbo].[CourseIndicator] OFF
GO
SET IDENTITY_INSERT [dbo].[DisplayOrientation] ON 

INSERT [dbo].[DisplayOrientation] ([DisplayOrientationID], [DisplayOrientationChar], [DisplayOrientationName]) VALUES (1, N'C', N'Course-up')
INSERT [dbo].[DisplayOrientation] ([DisplayOrientationID], [DisplayOrientationChar], [DisplayOrientationName]) VALUES (2, N'H', N'Head-up')
INSERT [dbo].[DisplayOrientation] ([DisplayOrientationID], [DisplayOrientationChar], [DisplayOrientationName]) VALUES (3, N'N', N'North-up')
SET IDENTITY_INSERT [dbo].[DisplayOrientation] OFF
GO
SET IDENTITY_INSERT [dbo].[DistanceUnit] ON 

INSERT [dbo].[DistanceUnit] ([DistanceUnitID], [DistanceUnitChar], [DistanceUnitName]) VALUES (1, N'K', N'Kilometers')
INSERT [dbo].[DistanceUnit] ([DistanceUnitID], [DistanceUnitChar], [DistanceUnitName]) VALUES (2, N'N', N'Nautical miles')
INSERT [dbo].[DistanceUnit] ([DistanceUnitID], [DistanceUnitChar], [DistanceUnitName]) VALUES (3, N'S', N'Status miles')
SET IDENTITY_INSERT [dbo].[DistanceUnit] OFF
GO
SET IDENTITY_INSERT [dbo].[Iff] ON 

INSERT [dbo].[Iff] ([IffID], [IffChar], [IffName]) VALUES (1, N'b', N'FRIEND')
INSERT [dbo].[Iff] ([IffID], [IffChar], [IffName]) VALUES (2, N'p', N'FOE')
INSERT [dbo].[Iff] ([IffID], [IffChar], [IffName]) VALUES (3, N'd', N'UNKNOWN')
SET IDENTITY_INSERT [dbo].[Iff] OFF
GO
SET IDENTITY_INSERT [dbo].[NmeaFormat] ON 

INSERT [dbo].[NmeaFormat] ([FormatID], [FormatName]) VALUES (1, N'TTM')
INSERT [dbo].[NmeaFormat] ([FormatID], [FormatName]) VALUES (2, N'RSD')
INSERT [dbo].[NmeaFormat] ([FormatID], [FormatName]) VALUES (3, N'VHW')
SET IDENTITY_INSERT [dbo].[NmeaFormat] OFF
GO
SET IDENTITY_INSERT [dbo].[WorkingMode] ON

INSERT [dbo].[WorkingMode] ([WorkingModeID], [WorkingModeChar], [WorkingModeName]) VALUES (1, N'S', N'Preparation or CONTROL Mode')
INSERT [dbo].[WorkingMode] ([WorkingModeID], [WorkingModeChar], [WorkingModeName]) VALUES (2, N'P', N'Proceeding (Emitting)')
SET IDENTITY_INSERT [dbo].[WorkingMode] OFF
GO
SET IDENTITY_INSERT [dbo].[NmeaMessage] ON 

INSERT [dbo].[NmeaMessage] ([MessageID], [MessageFormatID], [MessageRecTime], [MessageTime], [Message]) VALUES (1, 1, CAST(N'2024-02-28T18:34:26.507' AS DateTime), 1709134466506, N'$RATTM,23,13.88,137.2,T,63.8,094.3,T,9.2,79.4,N,b,T,793344,A*42')
INSERT [dbo].[NmeaMessage] ([MessageID], [MessageFormatID], [MessageRecTime], [MessageTime], [Message]) VALUES (3, 2, CAST(N'2024-02-28T18:35:32.613' AS DateTime), NULL, N'$RARSD,50.5,309.9,64.8,132.3,,,,,52.6,155.0,48.0,K,N,S*28')
INSERT [dbo].[NmeaMessage] ([MessageID], [MessageFormatID], [MessageRecTime], [MessageTime], [Message]) VALUES (4, 3, CAST(N'2024-02-29T17:03:59.557' AS DateTime), NULL, N'$RAVHW,356.7,T,,,50.4,N,,*76')
INSERT [dbo].[NmeaMessage] ([MessageID], [MessageFormatID], [MessageRecTime], [MessageTime], [Message]) VALUES (11, 2, CAST(N'2024-03-06T16:44:59.077' AS DateTime), 0, N'$RARSD,50.5,309.9,64.8,132.3,,,,,52.6,155.0,48.0,K,N,S*28')
INSERT [dbo].[NmeaMessage] ([MessageID], [MessageFormatID], [MessageRecTime], [MessageTime], [Message]) VALUES (12, 3, CAST(N'2024-03-06T16:46:12.353' AS DateTime), 0, N'$RAVHW, 115.6,T,,,46.0,N,,*71')
INSERT [dbo].[NmeaMessage] ([MessageID], [MessageFormatID], [MessageRecTime], [MessageTime], [Message]) VALUES (32, 1, CAST(N'2024-03-06T17:39:26.260' AS DateTime), 1709735966260, N'$RATTM,54,19.1,139.7,T,07.4,084.1,T,2.1, -95.8,N,d,L,,849019,A*7e')
INSERT [dbo].[NmeaMessage] ([MessageID], [MessageFormatID], [MessageRecTime], [MessageTime], [Message]) VALUES (33, 2, CAST(N'2024-03-06T18:30:49.750' AS DateTime), 0, N'$RARSD,36.5,331.4,8.4,320.6,,,,,11.6,185.3,96.0,N,N,S*33')
SET IDENTITY_INSERT [dbo].[NmeaMessage] OFF
GO
INSERT [dbo].[Rsd] ([MessageID], [InitialDistance], [InitialBearing], [MovingCircleOfDistance], [Bearing], [DistanceFromShip], [Bearing2], [DistanceScale], [DistanceUnitID], [DisplayOrientationID], [WorkingModeID]) VALUES (3, 50.5, 309.9, 64.8, 132.3, 52.6, 155, 58, 1, 3, 1)
INSERT [dbo].[Rsd] ([MessageID], [InitialDistance], [InitialBearing], [MovingCircleOfDistance], [Bearing], [DistanceFromShip], [Bearing2], [DistanceScale], [DistanceUnitID], [DisplayOrientationID], [WorkingModeID]) VALUES (11, 50.5, 309.9, 64.8, 132.3, 52.6, 155, 48, 1, 3, 1)
INSERT [dbo].[Rsd] ([MessageID], [InitialDistance], [InitialBearing], [MovingCircleOfDistance], [Bearing], [DistanceFromShip], [Bearing2], [DistanceScale], [DistanceUnitID], [DisplayOrientationID], [WorkingModeID]) VALUES (33, 36.5, 331.4, 8.4, 320.6, 11.6, 185.3, 96, 2, 3, 1)
GO
SET IDENTITY_INSERT [dbo].[SpeedUnit] ON 

INSERT [dbo].[SpeedUnit] ([SpeedUnitID], [SpeedUnitChar], [SpeedUnitName]) VALUES (1, N'N', NULL)
SET IDENTITY_INSERT [dbo].[SpeedUnit] OFF
GO
SET IDENTITY_INSERT [dbo].[TargetStatus] ON 

INSERT [dbo].[TargetStatus] ([TargetStatusID], [StatusChar], [StatusName]) VALUES (1, N'L', N'LOST')
INSERT [dbo].[TargetStatus] ([TargetStatusID], [StatusChar], [StatusName]) VALUES (2, N'Q', N'UNRELIABLE_DATA')
INSERT [dbo].[TargetStatus] ([TargetStatusID], [StatusChar], [StatusName]) VALUES (3, N'T', N'TRACKED')
SET IDENTITY_INSERT [dbo].[TargetStatus] OFF
GO
SET IDENTITY_INSERT [dbo].[TargetType] ON 

INSERT [dbo].[TargetType] ([TargetTypeID], [TypeChar], [TypeName]) VALUES (1, N' ', N'SURFACE')
INSERT [dbo].[TargetType] ([TargetTypeID], [TypeChar], [TypeName]) VALUES (2, N' ', N'AIR')
INSERT [dbo].[TargetType] ([TargetTypeID], [TypeChar], [TypeName]) VALUES (3, N' ', N'UNKNOWN')
SET IDENTITY_INSERT [dbo].[TargetType] OFF
GO
INSERT [dbo].[Ttm] ([MessageID], [TargetNumber], [Distance], [Bearing], [Course], [CourseIndicatorID], [Speed], [TargetTypeID], [TargetStatusID], [IffID], [Interval]) VALUES (1, 23, 13.88, 137.2, 94.3, 1, 63.8, 3, 1, 1, 793344)
INSERT [dbo].[Ttm] ([MessageID], [TargetNumber], [Distance], [Bearing], [Course], [CourseIndicatorID], [Speed], [TargetTypeID], [TargetStatusID], [IffID], [Interval]) VALUES (32, 54, 19.1, 139.7, 84.1, 1, 7.4, 3, 1, 3, 849019)
GO
INSERT [dbo].[Vhw] ([MessageID], [Course], [CourseAttrID], [Speed], [SpeedUnitID]) VALUES (4, 356.7, 1, 50.4, 1)
INSERT [dbo].[Vhw] ([MessageID], [Course], [CourseAttrID], [Speed], [SpeedUnitID]) VALUES (12, 115.6, 1, 46, 1)
GO


create procedure [dbo].[pAddNmeaMessage]
(
	@format varchar(32),
	@recTime datetime,
	@time bigint,
	@message varchar(128)
)
as
begin
	declare @formatId int
	set @formatId = 
	(select [FormatID] from [dbo].[NmeaFormat]
	where [FormatName] = @format)

	insert into [dbo].[NmeaMessage]([MessageFormatID],[MessageRecTime],
	[MessageTime],[Message])
	values
	(@formatId,@recTime,@time,@message)
end;
GO
/****** Object:  StoredProcedure [dbo].[pAddRSD]    Script Date: 16.03.2024 12:30:25 ******/
create procedure [dbo].[pAddRSD]
(
	@id int,
	@InitDist float,
	@InitBear float,
	@MC float,
	@Bearing float,
	@DFS float,
	@Bearing2 float,
	@DS float,
	@DU varchar,
	@DO varchar,
	@WM varchar
)
as 
begin
	
	declare @DU_id int
	set @DU_id = (select [DistanceUnitID] from [dbo].[DistanceUnit]
	where [DistanceUnitChar] = @DU)

	declare @DO_id int
	set @DO_id = (select [DisplayOrientationID] from [dbo].[DisplayOrientation]
	where [DisplayOrientationChar] = @DO)

	declare @WM_id int
	set @WM_id = (select [WorkingModeID] from [dbo].[WorkingMode]
	where [WorkingModeChar] = @WM)

	insert into [dbo].[Rsd] values
	(@id,@InitDist,@InitBear,@MC,@Bearing,@DFS,@Bearing2,@DS,@DU_id,@DO_id,@WM_id)

end;
GO
CREATE procedure [dbo].[pAddTTM]
(
	@id int,
	@TN int,
	@Distance float,
	@Bearing float,
	@Course float,
	@CI varchar,
	@Speed float,
	@TT varchar(32),
	@TS varchar(32),
	@IFF varchar(32),
	@Interval bigint,
	@msgTime bigint
)
as
begin

	declare @CI_id int
	set @CI_id = (select [CourseIndicatorID] from [dbo].[CourseIndicator]
	where [CourseIndicatorChar] = @CI)

	declare @TT_id int
	set @TT_id = (select [TargetTypeID] from [dbo].[TargetType]
	where [TypeName] = @TT)

	declare @TS_id int
	set @TS_id = (select [TargetStatusID] from [dbo].[TargetStatus]
	where [StatusName] = @TS)

	declare @IFF_id int
	set @IFF_id = (select [IffID] from [dbo].[Iff]
	where [IffName] = @IFF)

	insert into [dbo].[Ttm] values
	(@id, @TN, @Distance, @Bearing, @Course, @CI_id, @Speed, @TT_id, @TS_id, @IFF_id, @Interval) 

	update NmeaMessage
	set MessageTime = @msgTime
	where MessageID = @id

end;
GO
create procedure [dbo].[pAddVHW]
(
	@id int,
	@Course float,
	@CA varchar,
	@Speed float,
	@SU varchar
)
as
begin

	declare @CA_id int
	set @CA_id = (select [CourseAttrID] from [dbo].[CourseAttr]
	where [CourseAttrChar] = @CA)

	declare @SU_id int
	set @SU_id = (select [SpeedUnitID] from [dbo].[SpeedUnit]
	where [SpeedUnitChar] = @SU)

	insert into [dbo].[Vhw] values
	(@id,@Course,@CA_id,@Speed,@SU_id)

end;
GO
create procedure [dbo].[pDeleteNmeaMessage]
(
	@id int
)
as
begin
	delete from [dbo].[NmeaMessage] 
	where [MessageID] = @id
end;

