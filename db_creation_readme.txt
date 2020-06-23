install mysql database
MAC:
https://dev.mysql.com/doc/mysql-osx-excerpt/5.7/en/osx-installation-pkg.html
WINDOWS:
https://dev.mysql.com/doc/refman/8.0/en/windows-installation.html
LINUX:
https://dev.mysql.com/doc/refman/8.0/en/linux-installation.html


Login to Mysql using MysqlClient

https://dev.mysql.com/doc/mysql-getting-started/en/


Run following command on terminal 
Mysql -u root -p

For password  enter 
root



create database employeeFeedback;

Use employeeFeedback;

create table Login(user_id varchar(100),password varchar(100), primary key (user_id));

Create table Rating(user_id varchar(100),manager_id varchar(100),Employee_Rating varchar(100), manager_rating varchar(100), firstName varchar(50),LastName varchar(50),Department varchar(50),Primary key(user_id,manager_id));
