create bookstore;
use bookstore;
create table publisher(id mediumint not null, name varchar(30), primary key(id));
create table book(id int not null, isbn char(10) not null, title varchar(200), publisher_id mediumint, year date, stock integer(3), price decimal(5,2), description varchar(255), primary key(id), foreign key(publisher_id) references publisher(id), constraint c_price check(price >= 0), constraint c_stock check(stock >= 0), unique(isbn));
create table author(id mediumint not null, isbn char(10), name varchar(35), primary key(id), foreign key(isbn) references book(isbn));
CREATE USER 'soatest'@'%' IDENTIFIED BY 'soatest';
GRANT SELECT ON bookstore.* TO 'soatest'@'%' IDENTIFIED BY 'soatest';
FLUSH PRIVILEGES;