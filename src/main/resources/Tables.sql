--h2 is typically used to setup a test database, not a prod database.
--first, drop your tables (to reset your database for testing)
--then create your tables
<<<<<<< HEAD

DROP TABLE IF EXISTS bank_user
CREATE TABLE bank_user{
user_Id int primary key auto_increment,
username varchar(50) NOT NULL
password varchar(50) NOT NULL
}
DROP TABLE IF EXISTS account
CREATE TABLE account(
account_id int primary key auto_increment,
balance decimal,
account_user int FOREIGN KEY REFERENCES bank_user(id)

)
DROP TABLE IF EXISTS transactions
CREATE TABLE transactions(
   transaction_id int auto_increment,
   transaction_type varchar(50),
   amount decimal,
   transaction_time timestamp,
   account_user int FOREIGN KEY REFERENCES bank_user(user_Id),
   account_id int FOREIGN KEY REFERENCES account(account_id)

)
=======
drop table if exists bank_user;
drop table if exists account;

create table bank_user (
    user_id int primary key auto_increment,
    username varchar(255) unique,
    password varchar(255)
);

create table account (
    account_id int primary key auto_increment,
    balance decimal,
    account_user int,
    foreign key (account_user) references bank_user(user_id)
);

create table transactions (
    transaction_id int primary key auto_increment,
    transaction_type varchar(255),
    amount decimal,
    transaction_time timestamp,
    account_user int,
    foreign key (account_user) references bank_user(user_id),
    account_id int,
    foreign key (account_id) references account (account_id)
);
>>>>>>> d7b95aa63c650cd003e4dbada6a31dcdff56a159

