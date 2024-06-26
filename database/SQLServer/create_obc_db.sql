USE [master]
GO

/****** Object:  Database [obc_db]    Script Date: 2/14/2024 7:30:23 AM ******/
CREATE DATABASE [obc_db] ON  PRIMARY
( NAME = N'obc_db', FILENAME = N'K:\MSSQL10.MSSQLSERVER\MSSQL\DATA\obc_db.mdf' , SIZE = 256000KB , MAXSIZE = UNLIMITED, FILEGROWTH = 102400KB ),
 FILEGROUP [INDEX_FG]
( NAME = N'index_fg1', FILENAME = N'K:\MSSQL10.MSSQLSERVER\MSSQL\DATA\index_fg1.ndf' , SIZE = 51200KB , MAXSIZE = UNLIMITED, FILEGROWTH = 10240KB )
 LOG ON
( NAME = N'obc_db_log', FILENAME = N'L:\MSSQL10.MSSQLSERVER\MSSQL\Data\obc_db_log.ldf' , SIZE = 256000KB , MAXSIZE = 10485760KB , FILEGROWTH = 102400KB )
GO

IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [obc_db].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO

ALTER DATABASE [obc_db] SET ANSI_NULL_DEFAULT OFF
GO

ALTER DATABASE [obc_db] SET ANSI_NULLS OFF
GO

ALTER DATABASE [obc_db] SET ANSI_PADDING OFF
GO

ALTER DATABASE [obc_db] SET ANSI_WARNINGS OFF
GO

ALTER DATABASE [obc_db] SET ARITHABORT OFF
GO

ALTER DATABASE [obc_db] SET AUTO_CLOSE OFF
GO

ALTER DATABASE [obc_db] SET AUTO_SHRINK OFF
GO

ALTER DATABASE [obc_db] SET AUTO_UPDATE_STATISTICS ON
GO

ALTER DATABASE [obc_db] SET CURSOR_CLOSE_ON_COMMIT OFF
GO

ALTER DATABASE [obc_db] SET CURSOR_DEFAULT  GLOBAL
GO

ALTER DATABASE [obc_db] SET CONCAT_NULL_YIELDS_NULL OFF
GO

ALTER DATABASE [obc_db] SET NUMERIC_ROUNDABORT OFF
GO

ALTER DATABASE [obc_db] SET QUOTED_IDENTIFIER OFF
GO

ALTER DATABASE [obc_db] SET RECURSIVE_TRIGGERS OFF
GO

ALTER DATABASE [obc_db] SET  DISABLE_BROKER
GO

ALTER DATABASE [obc_db] SET AUTO_UPDATE_STATISTICS_ASYNC OFF
GO

ALTER DATABASE [obc_db] SET DATE_CORRELATION_OPTIMIZATION OFF
GO

ALTER DATABASE [obc_db] SET TRUSTWORTHY OFF
GO

ALTER DATABASE [obc_db] SET ALLOW_SNAPSHOT_ISOLATION OFF
GO

ALTER DATABASE [obc_db] SET PARAMETERIZATION SIMPLE
GO

ALTER DATABASE [obc_db] SET READ_COMMITTED_SNAPSHOT OFF
GO

ALTER DATABASE [obc_db] SET HONOR_BROKER_PRIORITY OFF
GO

ALTER DATABASE [obc_db] SET RECOVERY FULL
GO

ALTER DATABASE [obc_db] SET  MULTI_USER
GO

ALTER DATABASE [obc_db] SET PAGE_VERIFY CHECKSUM
GO

ALTER DATABASE [obc_db] SET DB_CHAINING OFF
GO

ALTER DATABASE [obc_db] SET  READ_WRITE
GO
