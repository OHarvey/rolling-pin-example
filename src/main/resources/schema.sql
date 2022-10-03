CREATE TABLE myDatabase.dbo.MyTable (
        PersonID INT,
        LastName VARCHAR(255),
        FirstName VARCHAR(255),
        Address VARCHAR(255),
        City VARCHAR(255)
);

-- Should ignore this line!

CREATE TABLE myDatabase.dbo.MyOtherTable (
        PersonID INT NOT NULL,
        City VARCHAR(255)
);

CREATE TABLE otherDatabase.dbo.City (
        id INT PRIMARY IDENTITY NOT NULL,
        City VARCHAR(255)
);
