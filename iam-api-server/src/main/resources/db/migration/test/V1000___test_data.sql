insert into REALMS(ID,NAME,DESCRIPTION,CONFIG,CREATION_TIME,LAST_UPDATE_TIME, REGISTRATION_ENABLED, PRIVACY_POLICY_URL, AUP_URL) 
    values (1,'iam', 'The iam realm', '{}', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), false, 
    'https://iam.example.com/privacy', null);
 
insert into REALMS(ID,NAME,DESCRIPTION,CONFIG,CREATION_TIME,LAST_UPDATE_TIME, REGISTRATION_ENABLED, PRIVACY_POLICY_URL, AUP_URL) 
    values (2,'alice', 'The alice realm', '{}', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), true, 
    'https://alice.example.com/privacy', 'https://alice.example.com/aup');
    
insert into REALMS(ID,NAME,DESCRIPTION,CONFIG,CREATION_TIME,LAST_UPDATE_TIME, REGISTRATION_ENABLED, PRIVACY_POLICY_URL, AUP_URL) 
    values (3,'atlas', 'The atlas realm', '{}', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), false,
    'https://atlas.example.com/privacy', 'https://atlas.example.com/aup');

insert into REALMS(ID,NAME,DESCRIPTION,CONFIG,CREATION_TIME,LAST_UPDATE_TIME,REGISTRATION_ENABLED, PRIVACY_POLICY_URL, AUP_URL)
    values (4,'cms', 'The cms realm', '{}', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), false,
        'https://cms.example.com/privacy', 'https://cms.example.com/aup');
 
insert into REALMS(ID,NAME,DESCRIPTION,CONFIG,CREATION_TIME,LAST_UPDATE_TIME,REGISTRATION_ENABLED, PRIVACY_POLICY_URL, AUP_URL)
    values (5,'lhcb', 'The lhcb realm', '{}', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), true,
        'https://lhcb.example.com/privacy', 'https://lhcb.example.com/aup');

insert into GROUPS(ID,REALM_ID,NAME,UUID,PARENT_GROUP_ID,CREATION_TIME,LAST_UPDATE_TIME)
    values (100,2,'alice', 'c6fe138f-2f55-4d1d-b2b5-968bdf3bdff4', null,CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

insert into GROUPS(ID,REALM_ID,NAME,UUID,PARENT_GROUP_ID,CREATION_TIME,LAST_UPDATE_TIME)
    values (101,2,'alice/production', '4b98e830-6e46-4051-b492-4f970d8c9260', 100,CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

insert into USERS(ID,REALM_ID,UUID,USERNAME,GIVEN_NAME,FAMILY_NAME,ACTIVE,PROVISIONED,CREATION_TIME,LAST_UPDATE_TIME,PASSWORD)
    values 
    (1000,2,'4868478e-f79e-40fc-83a9-48b8d91cd321','test-user','Test','User',true,false,
    CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'password');

insert into USERS_EMAILS(USER_ID,EMAIL,IS_PRIMARY,EMAIL_VERIFIED)
    values 
    (1000,'test@example.com', true, true),
    (1000,'test@other.example.com', false, false);
