INSERT INTO configurations (Id, Key, Value, Version,Enviornment) VALUES ('1', 'V1_All', 'V1_All', '1','ALL');
INSERT INTO configurations (Id, Key, Value, Version,Enviornment) VALUES ('2', 'V1_Dev', 'V1_Dev', '1','DEVELOPEMENT');
INSERT INTO configurations (Id, Key, Value, Version,Enviornment) VALUES ('3', 'V_All_All', 'V_All_All', 'ALL','ALL');
INSERT INTO configurations (Id, Key, Value, Version,Enviornment) VALUES ('4', 'V_All_Dev', 'V_All_Dev', 'ALL','DEVELOPEMENT');
INSERT INTO configurations (Id, Key, Value, Version,Enviornment) VALUES ('5', 'V_All_QA', 'V_All_Dev', 'ALL','QA');
INSERT INTO configurations (Id, Key, Value, Version,Enviornment) VALUES ('6', 'V_2_Dev', 'V_2_Dev', '2','QA');
INSERT INTO configurations (Id, Key, Value, Version,Enviornment) VALUES ('7', 'V_2_QA', 'V_2_QA', '2','QA');
INSERT INTO configurations (Id, Key, Value, Version,Enviornment) VALUES ('8', 'DefaultAuthenticationProvider', 'DEFAULT', 'ALL','ALL');
INSERT INTO configurations (Id, Key, Value, Version,Enviornment) VALUES ('9', 'V1_All_B', 'V1_All_B', '1','ALL');
INSERT INTO configurations (Id, Key, Value, Version,Enviornment) VALUES ('10', 'SessionTimeOutInMinutes', '20', 'ALL','ALL');

INSERT INTO Users(UserId,PasswordHash,AuthenticationProvider,ExternalSystemId)VALUES ('DEFAULT:ANNONYMOUS',0,'DEFAULT','ANNONYMOUS');
INSERT INTO Users(UserId,PasswordHash,AuthenticationProvider,ExternalSystemId)VALUES ('DEFAULT:ADMIN',0,'DEFAULT','ADMIN');
INSERT INTO Users(UserId,PasswordHash,AuthenticationProvider,ExternalSystemId)VALUES ('DEFAULT:BACKGROUND',0,'DEFAULT','BACKGROUND');