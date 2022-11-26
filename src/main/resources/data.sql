-- seed articles in database
INSERT INTO club (name, capacity) VALUES ('Muziek-o-droom', 1800);

-- Create user table
CREATE TABLE `Users` (
                         `idUsers` int NOT NULL,
                         `name` varchar(45) DEFAULT NULL,
                         `password` varchar(100) DEFAULT NULL,
                         `enabled` int DEFAULT NULL,
                         `role` varchar(45) DEFAULT NULL, PRIMARY KEY ('idUsers'));

// insert users
    INSERT INTO Users (idUsers,name,password,enabled,role)
    VALUES (1, 'admin', '$2a$12$xd5Q4DmSz0jJ7YIozfAz3.oc0TfN.QKk.qb/ZxBk6jKlLQtkd7eoe',1,'ROLE_ADMIN');
    INSERT INTO Users (idUsers,name,password,enabled,role)
    VALUES (2, 'test', '$2a$12$xd5Q4DmSz0jJ7YIozfAz3.oc0TfN.QKk.qb/ZxBk6jKlLQtkd7eoe',1,'ROLE_TEST');
    INSERT INTO Users (idUsers,name,password,enabled,role)
    VALUES (3, 'user', '$2a$12$xd5Q4DmSz0jJ7YIozfAz3.oc0TfN.QKk.qb/ZxBk6jKlLQtkd7eoe',1,'ROLE_USER');
