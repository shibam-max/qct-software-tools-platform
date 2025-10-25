-- SQL Server initialization script for QCT User Management
-- This script creates the database schema for user management

USE master;
GO

-- Create database
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'QCT_Users')
BEGIN
    CREATE DATABASE QCT_Users;
END
GO

USE QCT_Users;
GO

-- Create Users table
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Users')
BEGIN
    CREATE TABLE Users (
        Id INT IDENTITY(1,1) PRIMARY KEY,
        Username NVARCHAR(50) NOT NULL UNIQUE,
        Email NVARCHAR(100) NOT NULL UNIQUE,
        PasswordHash NVARCHAR(255) NOT NULL,
        FirstName NVARCHAR(50) NULL,
        LastName NVARCHAR(50) NULL,
        IsActive BIT NOT NULL DEFAULT 1,
        CreatedAt DATETIME2 NOT NULL DEFAULT GETUTCDATE(),
        UpdatedAt DATETIME2 NOT NULL DEFAULT GETUTCDATE()
    );
END
GO

-- Create UserSessions table for tracking active sessions
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'UserSessions')
BEGIN
    CREATE TABLE UserSessions (
        Id INT IDENTITY(1,1) PRIMARY KEY,
        UserId INT NOT NULL,
        Token NVARCHAR(500) NOT NULL,
        ExpiresAt DATETIME2 NOT NULL,
        CreatedAt DATETIME2 NOT NULL DEFAULT GETUTCDATE(),
        FOREIGN KEY (UserId) REFERENCES Users(Id)
    );
END
GO

-- Create indexes for performance
CREATE NONCLUSTERED INDEX IX_Users_Email ON Users(Email);
CREATE NONCLUSTERED INDEX IX_Users_Username ON Users(Username);
CREATE NONCLUSTERED INDEX IX_UserSessions_Token ON UserSessions(Token);
CREATE NONCLUSTERED INDEX IX_UserSessions_UserId ON UserSessions(UserId);
GO

-- Insert sample data
INSERT INTO Users (Username, Email, PasswordHash, FirstName, LastName) VALUES
('admin', 'admin@qct.com', '$2a$11$8gE7mKKW8yZZkZqZqZqZqOJ8J8J8J8J8J8J8J8J8J8J8J8J8J8J8J8', 'Admin', 'User'),
('developer', 'dev@qct.com', '$2a$11$8gE7mKKW8yZZkZqZqZqZqOJ8J8J8J8J8J8J8J8J8J8J8J8J8J8J8J8', 'Developer', 'User'),
('tester', 'test@qct.com', '$2a$11$8gE7mKKW8yZZkZqZqZqZqOJ8J8J8J8J8J8J8J8J8J8J8J8J8J8J8J8', 'Test', 'User');
GO