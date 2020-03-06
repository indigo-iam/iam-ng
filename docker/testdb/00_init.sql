CREATE DATABASE IF NOT EXISTS kc;
CREATE DATABASE IF NOT EXISTS iam;

GRANT ALL PRIVILEGES ON kc.* to 'kc'@'%' identified by 'pwd';
GRANT ALL PRIVILEGES ON iam.* to 'iam'@'%' identified by 'pwd';
