Create DATABASE Dalton
Go

Use [Dalton]

Create Table ReferralGroup (
    ReferralGroupID INT IDENTITY(1,1) Primary Key,
    ReferralTypeName VARCHAR(200) Not NULL,
    Active BIT Not Null
)

Create Table ReferralType (
    ReferralTypeID INT IDENTITY(1,1) Primary Key,
    ReferralTypeName VARCHAR(200) NOT NULL,
    Active BIT Not Null,
    RGID INT NOT NULL FOREIGN KEY REFERENCES ReferralGroup(ReferralGroupID)
)

Create Table Users (
    UserID INT PRIMARY KEY,
    LastName VARCHAR(50) NOT NULL,
    FirstName VARCHAR(50) NOT NULL
)

Create Table Workers (
    UserID INT NOT NULL FOREIGN KEY REFERENCES Users(UserID),
    RTID INT NOT NULL FOREIGN KEY REFERENCES  ReferralType(ReferralTypeID),
    PRIMARY KEY (UserID, RTID)
)

Create Table REF_Status (
    StatusID INT Primary KEY,
    StatusDescription VARCHAR(25) not NULL
)

Create Table Referrals (
    ReferralID INT IDENTITY(1,1) PRIMARY Key,
    RequesterID INT NOT NULL FOREIGN KEY REFERENCES Users(UserID),
    WorkerID INT FOREIGN KEY REFERENCES Users(UserID),
    RTID INT NOT NULL FOREIGN KEY REFERENCES ReferralType(ReferralTypeID)
)

Create Table ReferralStatus (
    ReferralStatusID INT IDENTITY(1,1) PRIMARY KEY,
    StatusID INT NOT NULL FOREIGN KEY REFERENCES REF_Status(StatusID),
    IsCurrentStatus BIT NOT NULL,
    StatusUpdateDate DATETIME2 NOT NULL,
    RID INT NOT NULL FOREIGN KEY REFERENCES Referrals(ReferralID)
)

Go

Use [Dalton]

INSERT INTO ReferralGroup Values('Premium Accounting',1)
INSERT INTO ReferralGroup Values('Underwriting', 1)
INSERT INTO ReferralGroup Values('Help Desk', 1)

INSERT INTO ReferralType Values('Alter Billing Address', 1, 1)
INSERT INTO ReferralType Values('Disputed Activity', 1, 2)
INSERT INTO ReferralType Values('Correspondence', 1, 3)
INSERT INTO ReferralType Values('Check Requisition', 1, 1)

INSERT INTO Users Values(12345, 'Dillon', 'Matt')
INSERT INTO Users Values(67890, 'Russell', 'Kitty')
INSERT INTO Users Values(11111, 'Hagen', 'Festus')

INSERT INTO Workers Values(67890, 1)
INSERT INTO Workers Values(67890, 4)

INSERT INTO Referrals Values(11111, Null, 1)
INSERT INTO Referrals Values(11111, Null, 4)
INSERT INTO Referrals Values(11111, Null, 2)
INSERT INTO Referrals Values(11111, 67890, 4)

Insert Into REF_Status Values(1, 'New Request')
Insert Into REF_Status Values(2, 'Recieved')
Insert Into REF_Status Values(3, 'Assigned')
Insert Into REF_Status Values(4, 'In Progress')
Insert Into REF_Status Values(5, 'Completed')
Insert Into REF_Status Values(6, 'Declined')

INSERT INTO ReferralStatus Values(1, 0, '2019-11-6 08:00:00', 1)
INSERT INTO ReferralStatus Values(2, 1, '2019-11-6 08:05:00', 1)
INSERT INTO ReferralStatus Values(1, 0, '2019-11-6 10:17:00', 2)
INSERT INTO ReferralStatus Values(2, 1, '2019-11-6 10:22:00', 2)
INSERT INTO ReferralStatus Values(1, 0, '2019-11-7 14:00:00', 3)
INSERT INTO ReferralStatus Values(2, 1, '2019-11-7 14:09:00', 3)
INSERT INTO ReferralStatus Values(1, 0, '2019-11-8 19:00:00', 4)
INSERT INTO ReferralStatus Values(1, 0, '2019-11-8 19:03:00', 4)
INSERT INTO ReferralStatus Values(3, 1, '2019-11-9 11:17:00', 4)

Go