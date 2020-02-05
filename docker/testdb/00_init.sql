CREATE DATABASE IF NOT EXISTS iam;
CREATE DATABASE IF NOT EXISTS one;
CREATE DATABASE IF NOT EXISTS two;

GRANT ALL PRIVILEGES ON iam.* to 'iam'@'%' identified by 'pwd';
GRANT ALL PRIVILEGES ON one.* to 'one'@'%' identified by 'pwd';
GRANT ALL PRIVILEGES ON two.* to 'two'@'%' identified by 'pwd';
