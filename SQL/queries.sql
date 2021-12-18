/* --- Search.java --- */
/* Search all books */
select id, isbn, title, author, genre, price, publisher, quantity
from book order by title;

/* Search books by title */
select * from book
where lower(title) like lower(?);

/* Search books by genre */
select * from book
where lower(author) like lower(?);

/* Search books by genre */
select * from book
where lower(genre) like lower(?);

/* --- Customer.java --- */
/* Login Check */
select * from customer
where username = ? and password = ?;

/* Register Check */
select count (*)
from customer;

/* Register customer */
insert into customer (username, password, card_no, billing_addr, shipping_addr) values (?, ?, ?, ?, ?);

/* Check for Book */
select * from book
where ID = ?;

/* Update book stock */
update book
set quantity = quantity-?
where ID = ?;

/* Add book to basket */
insert into basket (username, book_id, book_title, price, quantity) values (?, ?, ?, ?, ?);

/* View Basket */
select book_id, book_title, quantity, price, 'genre', 'author', 'publisher' from basket where username = ?;

/* Check order count for next ID */
select count (*)
from orders;

/* Retireve customer shipping address */
select shipping_addr
from customer
where username = ?;

/* Create order tuple */
insert into orders (order_number, username, origin, destination, date_ordered, status) values (?, ?, ?, ?, ?, ?);

/* Join basket and book to retireve information for sale tuple to be created */
select book_id, book_title, genre, author, publisher, basket.quantity, basket.price
from basket inner join book
on username = ? and book.title = book_title;

/* Clear basket after order is complete */
delete from basket where username = ?;

/* Retieve order information to track an order given order number */
select order_number, username, date_ordered, origin, destination, status
from orders
where order_number = ? and username = ?;

/* --- Manager.java --- */
/* Check existence of a publisher before adding a book */
select * from publisher
where name = ?;

/* Add new publisher tuple if publisher is not in database */
insert into publisher (name, address, email, phone_no, bank_acc) values (?, ?, ?, ?, ?);

/* Check book count for new book ID */
select count (*) from book;

/* Insert new book tuple */
insert into book (isbn, title, author, genre, price, publisher, number_pages, quantity, commission, id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);

/* Look for book to delete by ID */
select * from book where id = ?;

/* Delete book with given ID */
delete from book where id = ?;

/* Query information for Best Sellers Report */
select book_title, sum(quantity) as units_sold, sum(sold_for) as total_revenue
from sale
group by book_title
order by total_revenue desc;

/* Query information for Sales per Genre Report */
select book_genre, sum(quantity) as units_sold, sum(sold_for) as total_revenue
from sale
group by book_genre
order by total_revenue desc;

/* Query information for Sales per Author Report */
select book_author, sum(quantity) as units_sold, sum(sold_for) as total_revenue
from sale
group by book_author
order by total_revenue desc;

/* Query information for Publisher Commission Report */
select name, address, email, phone_no, bank_acc
from publisher
order by bank_acc desc;

/* --- BackEndOps.java --- */

/* Select information (namely total price of units in basket) to then calculate commission for the publisher of the book ordered */
select title, basket.price, commission, publisher
from book inner join basket
on book.title = basket.book_title and username = ?;

/* Add the calculated commission from the sale to the publisher's bank_acc */
update publisher
set bank_acc = bank_acc + ?
where name = ?;

/* Count sales for next sale_id */
select count (*) from sale;

/* Insert sale tuple */
insert into sale (sale_id, book_id, book_title, book_genre, book_author, book_publisher, quantity, sold_for) values (?, ?, ?, ?, ?, ?, ?, ?);

/* --- Triggers.java --- */
/* Check quantity of stock for a given book */
select quantity from book where title = ?;

/* Increase stock of book by a given amount */
update book set quantity = quantity + ? where title = ?;
