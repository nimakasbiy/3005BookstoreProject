Nima Gaffuri Kasbiy
101045743
COMP 3005 A

----- Bookstore Project ------

Purpose: A simple implementation of a bookstore using a combination of Java and SQL facilitated by JDBC. (This is a command line application, interacting with this application is done solely through the command line, following the options that the menus provide).

-------------------------------

Instructions to Run: 

1) Download the code directory from github and save it locally following the same file structure as it is organized in github.


2) On your local instane of postgres, create a new database called "3005final" listening on port 5432 with the following credentials: 

username: postgres
password: password


3) Make sure your postgres server is running, on your local instance of postgres, run the following .sql files in the following order to set up that database: 
	bookstore_DDL.sql
	customer.sql
	books.sql
	publishers.sql



4) navigate to the /company directory of the project and run the following command: 

javac *.java


5) navigate back up to the /src directory and run the following command to run the program: 

java -cp <YOUR LOCAL PATH TO THE /src DIRECTORY>/postgresql-42.2.24.jar:.: com.company.main

