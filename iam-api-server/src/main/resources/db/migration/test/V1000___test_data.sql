insert into REALMS(ID,NAME,DESCRIPTION,CONFIG,CREATION_TIME,LAST_UPDATE_TIME, REGISTRATION_ENABLED, PRIVACY_POLICY_URL, AUP_URL) 
    values (1,'iam', 'The iam realm', '{"kc":{"clientId":"iam-api", "clientSecret": "secret"}}', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), false, 
    'https://iam.example.com/privacy', null);
 
insert into REALMS(ID,NAME,DESCRIPTION,CONFIG,CREATION_TIME,LAST_UPDATE_TIME, REGISTRATION_ENABLED, PRIVACY_POLICY_URL, AUP_URL) 
    values (2,'alice', 'The alice realm', '{"kc":{"clientId":"iam-api", "clientSecret": "secret"}}', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), true, 
    'https://alice.example.com/privacy', 'https://alice.example.com/aup');
    
insert into REALMS(ID,NAME,DESCRIPTION,CONFIG,CREATION_TIME,LAST_UPDATE_TIME, REGISTRATION_ENABLED, PRIVACY_POLICY_URL, AUP_URL) 
    values (3,'atlas', 'The atlas realm', '{"kc":{"clientId":"iam-api", "clientSecret": "secret"}}', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), false,
    'https://atlas.example.com/privacy', 'https://atlas.example.com/aup');

insert into REALMS(ID,NAME,DESCRIPTION,CONFIG,CREATION_TIME,LAST_UPDATE_TIME,REGISTRATION_ENABLED, PRIVACY_POLICY_URL, AUP_URL)
    values (4,'cms', 'The cms realm', '{"kc":{"clientId":"iam-api", "clientSecret": "secret"}}', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), false,
        'https://cms.example.com/privacy', 'https://cms.example.com/aup');
 
insert into REALMS(ID,NAME,DESCRIPTION,CONFIG,CREATION_TIME,LAST_UPDATE_TIME,REGISTRATION_ENABLED, PRIVACY_POLICY_URL, AUP_URL)
    values (5,'lhcb', 'The lhcb realm', '{"kc":{"clientId":"iam-api", "clientSecret": "secret"}}', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), true,
        'https://lhcb.example.com/privacy', 'https://lhcb.example.com/aup');

