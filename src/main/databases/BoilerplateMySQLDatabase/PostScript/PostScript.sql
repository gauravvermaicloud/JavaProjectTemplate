INSERT INTO configurations (Id, ConfigurationKey, ConfigurationValue, Version,Enviornment) VALUES ('1', 'V1_All', 'V1_All', '1','ALL');
INSERT INTO configurations (Id, ConfigurationKey, ConfigurationValue, Version,Enviornment) VALUES ('2', 'V1_Dev', 'V1_Dev', '1','DEVELOPEMENT');
INSERT INTO configurations (Id, ConfigurationKey, ConfigurationValue, Version,Enviornment) VALUES ('3', 'V_All_All', 'V_All_All', 'ALL','ALL');
INSERT INTO configurations (Id, ConfigurationKey, ConfigurationValue, Version,Enviornment) VALUES ('4', 'V_All_Dev', 'V_All_Dev', 'ALL','DEVELOPEMENT');
INSERT INTO configurations (Id, ConfigurationKey, ConfigurationValue, Version,Enviornment) VALUES ('5', 'V_All_QA', 'V_All_Dev', 'ALL','QA');
INSERT INTO configurations (Id, ConfigurationKey, ConfigurationValue, Version,Enviornment) VALUES ('6', 'V_2_Dev', 'V_2_Dev', '2','QA');
INSERT INTO configurations (Id, ConfigurationKey, ConfigurationValue, Version,Enviornment) VALUES ('7', 'V_2_QA', 'V_2_QA', '2','QA');
INSERT INTO configurations (Id, ConfigurationKey, ConfigurationValue, Version,Enviornment) VALUES ('8', 'DefaultAuthenticationProvider', 'DEFAULT', 'ALL','ALL');
INSERT INTO configurations (Id, ConfigurationKey, ConfigurationValue, Version,Enviornment) VALUES ('9', 'V1_All_B', 'V1_All_B', '1','ALL');
INSERT INTO configurations (Id, ConfigurationKey, ConfigurationValue, Version,Enviornment) VALUES ('10', 'SessionTimeOutInMinutes', '20', 'ALL','ALL');

INSERT INTO Users(Id,UserId,PasswordHash,AuthenticationProvider,ExternalSystemId)VALUES (1,'DEFAULT:ANNONYMOUS',0,'DEFAULT','DEFAULT:ANNONYMOUS');
INSERT INTO Users(Id,UserId,PasswordHash,AuthenticationProvider,ExternalSystemId)VALUES (2,'DEFAULT:ADMIN',0,'DEFAULT','DEFAULT:ADMIN');
INSERT INTO Users(Id,UserId,PasswordHash,AuthenticationProvider,ExternalSystemId)VALUES (3,'DEFAULT:BACKGROUND',0,'DEFAULT','DEFAULT:BACKGROUND');
INSERT INTO Users(Id,UserId,PasswordHash,AuthenticationProvider,ExternalSystemId)VALUES (4,'DEFAULT:ROLEASSIGNER',0,'DEFAULT','DEFAULT:ROLEASSIGNER');

INSERT INTO Roles (Id,RoleName,RoleDescription,IsSystemRole,IsSelfAssign) VALUES (1,'Admin','Admin of the system',true,false);
INSERT INTO Roles (Id,RoleName,RoleDescription,IsSystemRole,IsSelfAssign) VALUES (2,'RoleAssigner','This role can assign roles to other users',true,false);

INSERT INTO Roles (Id,RoleName,RoleDescription,IsSystemRole,IsSelfAssign) VALUES (3,'SelfAssign1','UT role',false,true);
INSERT INTO Roles (Id,RoleName,RoleDescription,IsSystemRole,IsSelfAssign) VALUES (4,'SelfAssign2','UT role',false,true);
INSERT INTO Roles (Id,RoleName,RoleDescription,IsSystemRole,IsSelfAssign) VALUES (5,'NonSelfAssign1','UT role',false,false);
INSERT INTO Roles (Id,RoleName,RoleDescription,IsSystemRole,IsSelfAssign) VALUES (6,'NonSelfAssign2','UT role',false,false);


INSERT INTO UserRoles(Id,UserId,RoleId) VALUES(1,2,1); 
INSERT INTO UserRoles(Id,UserId,RoleId) VALUES(2,3,1); 
INSERT INTO UserRoles(Id,UserId,RoleId) VALUES(3,4,2); 