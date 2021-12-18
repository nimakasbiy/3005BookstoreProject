create table book
	(id             numeric(5,0) check (id > 0),
     isbn           varchar(13),
     title          varchar(50),
     author         varchar(50),
     genre          varchar(50),
     price          numeric(4,2) check (price > 0),
     publisher      varchar(50),
     number_pages   numeric(6,0) check (number_pages > 0),
     quantity       numeric(4,0) check (quantity > 0),
     commission     numeric(2,2) check (commission > 0 and commission < 1),
	 primary key (id)
	);

create table basket 
    (
     book_id        numeric(5,0) check (book_id > 0),
     book_title     varchar(50),
     username       varchar(15),
     quantity       numeric(4,0) check (quantity > 0),
     price          numeric(4,2) check (price > 0)
    );

create table customer
    (username       varchar(15),
     password       varchar(15),
     card_no        varchar(16),
     billing_addr   varchar(50),
     shipping_addr  varchar(50),
     primary key (username)
    );

create table orders
    (order_number   numeric(5,0) check (order_number > 0),
     username       varchar(15),
     date_ordered   varchar(15),
     origin         varchar(30),
     destination    varchar(30),
     status         varchar(15),
     primary key (order_number)
    );

create table publisher
    (name           varchar(50),
     address        varchar(50),
     email          varchar(50),
     phone_no       varchar(10),
     bank_acc       numeric(10,2) check (bank_acc >= 0),
     primary key (name)
    );

create table sale
    (sale_id        varchar(5),
     sold_for       numeric(4,2) check (sold_for > 0),
     quantity       numeric(5,0) check (quantity > 0),
     book_id        numeric(5,0),
     book_title     varchar(50),
     book_genre     varchar(50),
     book_author    varchar(50),
     book_publisher varchar(50),
     primary key (sale_id)
    );


