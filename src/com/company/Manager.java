package com.company;

import java.sql.*;
import java.util.Scanner;

public class Manager {

    /* Function that adds a publisher to the database using user-given parameters
       This function is only invoked when th user is in the manager menu and tries to add a book
       with a publisher not already in the database */
    static boolean addPublisher(String publisherName){
        Connection connection = null;
        Statement statement = null;
        int addPublisherResult;

        boolean result = false;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();
            {
                System.out.println("Publisher address: ");
                Scanner address_scn = new Scanner(System.in);
                String address = address_scn.nextLine();

                System.out.println("Publisher Email: ");
                Scanner email_scn = new Scanner(System.in);
                String email = email_scn.nextLine();

                System.out.println("Publisher Phone #: ");
                Scanner phone_scn = new Scanner(System.in);
                String phone = phone_scn.nextLine();
                
                try{
                    PreparedStatement pstmt3 = connection.prepareStatement("insert into publisher (name, address, email, phone_no, bank_acc)"
                            + " values (?, ?, ?, ?, ?)");
                    pstmt3.setString(1, publisherName);
                    pstmt3.setString(2, address);
                    pstmt3.setString(3, email);
                    pstmt3.setString(4, phone);
                    pstmt3.setFloat(5, 0);

                    addPublisherResult = pstmt3.executeUpdate();

                    if (addPublisherResult == 1){
                        System.out.println("Publisher succesfully added!");
                        result = true;
                    }

                }catch (Exception sqle) {
                    System.out.println("Could not add publisher: " + sqle);
                }

            }
        }catch (Exception sqle){
            System.out.println("Exception: " + sqle);
        }finally {
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};
            return result;
        }
    }

    /* Function that takes book information in from user and attempts to add the book to the database
       If the user inputs the name of a publisher that does not currently exist, addPublisher() is invoked */
    static void addBook(){
        Connection connection = null;
        Statement statement = null;
        ResultSet publisherResult = null;
        ResultSet bookCount = null;
        int addBookResult;
        int bookID;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();
            {
                System.out.println("ISBN: ");
                Scanner isbn_scn = new Scanner(System.in);
                String isbn = isbn_scn.next();

                System.out.println("Book Title: ");
                Scanner title_scn = new Scanner(System.in);
                String title = title_scn.nextLine();

                System.out.println("Book Author: ");
                Scanner author_scn = new Scanner(System.in);
                String author = author_scn.nextLine();

                System.out.println("Book Genre: ");
                Scanner genre_scn = new Scanner(System.in);
                String genre = genre_scn.nextLine();

                System.out.println("Price: ");
                Scanner price_scn = new Scanner(System.in);
                float price = price_scn.nextFloat();

                System.out.println("Publisher: ");
                Scanner publisher_scn = new Scanner(System.in);
                String publisher = publisher_scn.nextLine();

                System.out.println("# of pages: ");
                Scanner pages_scn = new Scanner(System.in);
                int pages = pages_scn.nextInt();

                System.out.println("Quantity: ");
                Scanner quantity_scn = new Scanner(System.in);
                int quantity = quantity_scn.nextInt();

                System.out.println("Commission % per sale: ");
                Scanner commission_scn = new Scanner(System.in);
                float commission = commission_scn.nextFloat();


                PreparedStatement pstmt = connection.prepareStatement("select * from publisher where name = ?;");
                pstmt.setString(1, publisher);
                publisherResult = pstmt.executeQuery();
                if (!publisherResult.next()){
                    System.out.println("The publisher is not found, please add the publisher info.");
                    if (!addPublisher(publisher)){
                        return;
                    }
                }

                PreparedStatement pstmt2 = connection.prepareStatement("select count (*) from book;");
                bookCount = pstmt2.executeQuery();

                if (!bookCount.next()){
                    bookID = 1;
                }
                else {
                    bookID = bookCount.getInt(1) + 1;
                }

                try{
                    PreparedStatement pstmt3 = connection.prepareStatement("insert into book (isbn, title, author, genre, price, publisher, number_pages, quantity, commission, id)"
                            + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    pstmt3.setString(1, isbn);
                    pstmt3.setString(2, title);
                    pstmt3.setString(3, author);
                    pstmt3.setString(4, genre);
                    pstmt3.setFloat(5, price);
                    pstmt3.setString(6, publisher);
                    pstmt3.setInt(7, pages);
                    pstmt3.setInt(8, quantity);
                    pstmt3.setFloat(9, commission);
                    pstmt3.setInt(10, bookID);

                    addBookResult = pstmt3.executeUpdate();

                    if (addBookResult == 1){
                        System.out.println("Book succesfully added!");
                    }

                }catch (Exception sqle) {
                    System.out.println("Could not add book: " + sqle);
                }

            }
        }catch(Exception sqle){
            System.out.println("Exception: " + sqle);
        }finally {
            try { if (publisherResult != null) publisherResult.close(); } catch (Exception e) {};
            try { if (bookCount != null) bookCount.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};
            Menu.managerMenu();
        }
    }

    /* Function that prompts user for the ID of the book they want to remove from the database.
       The function then attempts to delete the book from the database and returns to menu afterwards */
    static void removeBook(){
        Connection connection = null;
        ResultSet bookCheck = null;
        Statement statement = null;
        int bookID;
        int removeBookResult;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();
            {

                System.out.println("Enter Book ID of book to remove: ");
                Scanner remove_scn = new Scanner(System.in);
                bookID = remove_scn.nextInt();

                PreparedStatement pstmt = connection.prepareStatement("select * from book where id = ?");
                pstmt.setInt(1, bookID);
                bookCheck = pstmt.executeQuery();

                if (!bookCheck.next()){
                    System.out.println("No book ID: " + bookID);
                }

                try{
                    PreparedStatement pstmt2 = connection.prepareStatement("delete from book where id = ?");
                    pstmt2.setInt(1, bookID);

                    removeBookResult = pstmt2.executeUpdate();
                    if (removeBookResult == 1){
                        System.out.println("Book " + bookCheck.getString(3) + " has been removed.");
                    }

                }catch (Exception sqle){
                    System.out.println("Book Removal Failed: " + sqle);
                }
            }
        }catch(Exception sqle){
            System.out.println("Exception: " + sqle);
        }finally {
            try { if (bookCheck != null) bookCheck.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};
            Menu.managerMenu();
        }
    }

    /* Function that displays the Best Sellers report.
    * This report orders all books bought by the total amount of revenue they have brought to the store. */
    static void bestSellersReport(){
        Connection connection = null;
        Statement statement = null;
        ResultSet reportResult = null;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();
            {

                PreparedStatement pstmt = connection.prepareStatement("select book_title, sum(quantity) as units_sold, sum(sold_for) as total_revenue" +
                        " from sale group by book_title" +
                        " order by total_revenue desc;");
                reportResult = pstmt.executeQuery();

                if (!reportResult.next()){
                    System.out.println("No books have been sold. Report not available");
                }
                else{
                    System.out.println("-- REPORT: Best Selling Books --");
                    String headerString = String.format("|%-50s|%-15s|%-15s|", "Title", "Units Sold", "Total Revenue");
                    System.out.println(headerString);
                    do{
                        String title = reportResult.getString(1);
                        int unitsSold = reportResult.getInt(2);
                        float totalRevenue = reportResult.getFloat(3);
                        String resultString = String.format(" %-50s %-15s %-15s ", title, unitsSold, totalRevenue);
                        System.out.println(resultString);
                    } while(reportResult.next());
                }

            }
        }catch (Exception sqle){
            System.out.println("Exception: " + sqle);
        }finally {
            try { if (reportResult != null) reportResult.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};
            Menu.managerMenu();
        }
    }

    /* Function that displays the Sales per Genre report.
     * This report orders all genres of books bought by the total amount of revenue they have brought to the store. */
    static void salesPerGenreReport(){
        Connection connection = null;
        Statement statement = null;
        ResultSet reportResult = null;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();
            {

                PreparedStatement pstmt = connection.prepareStatement("select book_genre, sum(quantity) as units_sold, sum(sold_for) as total_revenue" +
                        " from sale group by book_genre" +
                        " order by total_revenue desc;");
                reportResult = pstmt.executeQuery();

                if (!reportResult.next()){
                    System.out.println("No books have been sold. Report not available");
                }
                else{
                    System.out.println("-- REPORT: Sales Per Genre --");
                    String headerString = String.format("|%-30s|%-15s|%-15s|", "Genre", "Units Sold", "Total Revenue");
                    System.out.println(headerString);
                    do{
                        String genre = reportResult.getString(1);
                        int unitsSold = reportResult.getInt(2);
                        float totalRevenue = reportResult.getFloat(3);
                        String resultString = String.format(" %-30s %-15s %-15s ", genre, unitsSold, totalRevenue);
                        System.out.println(resultString);
                    } while(reportResult.next());
                }

            }
        }catch (Exception sqle){
            System.out.println("Exception: " + sqle);
        }finally {
            try { if (reportResult != null) reportResult.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};
            Menu.managerMenu();
        }
    }

    /* Function that displays the Sales per Author report.
     * This report orders all Author of books bought by the total amount of revenue they have brought to the store. */
    static void salesPerAuthorReport(){
        Connection connection = null;
        Statement statement = null;
        ResultSet reportResult = null;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();
            {

                PreparedStatement pstmt = connection.prepareStatement("select book_author, sum(quantity) as units_sold, sum(sold_for) as total_revenue" +
                        " from sale group by book_author" +
                        " order by total_revenue desc;");
                reportResult = pstmt.executeQuery();

                if (!reportResult.next()){
                    System.out.println("No books have been sold. Report not available");
                }
                else{
                    System.out.println("-- REPORT: Sales Per Author --");
                    String headerString = String.format("|%-30s|%-15s|%-15s|", "Author", "Units Sold", "Total Revenue");
                    System.out.println(headerString);
                    do{
                        String author = reportResult.getString(1);
                        int unitsSold = reportResult.getInt(2);
                        float totalRevenue = reportResult.getFloat(3);
                        String resultString = String.format(" %-30s %-15s %-15s ", author, unitsSold, totalRevenue);
                        System.out.println(resultString);
                    } while(reportResult.next());
                }

            }
        }catch (Exception sqle){
            System.out.println("Exception: " + sqle);
        }finally {
            try { if (reportResult != null) reportResult.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};
            Menu.managerMenu();
        }
    }

    /* Function that displays the Publisher Commission report.
     * This report orders all Publishers by the total amount of commission they have earned from sales. */
    static void commissionReport(){
        Connection connection = null;
        Statement statement = null;
        ResultSet reportResult = null;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();
            {

                PreparedStatement pstmt = connection.prepareStatement("select name, address, email, phone_no, bank_acc from publisher" +
                        " order by bank_acc desc;");
                reportResult = pstmt.executeQuery();

                if (!reportResult.next()){
                    System.out.println("No publishers with commission to report.");
                }
                else{
                    System.out.println("-- REPORT: Publisher Commission Report --");
                    String headerString = String.format("|%-35s|%-35s|%-35s|%-20s|%-35s|", "Publisher Name", "Publisher Address", "Publisher e-mail", "Publisher Phone #", "Total Commission Earned");
                    System.out.println(headerString);
                    do{
                        String name = reportResult.getString(1);
                        String address = reportResult.getString(2);
                        String email = reportResult.getString(3);
                        String phone = reportResult.getString(4);
                        float commission = reportResult.getFloat(5);
                        String resultString = String.format(" %-35s %-35s %-35s %-20s %-35s ", name, address, email, phone, commission);
                        System.out.println(resultString);
                    } while(reportResult.next());
                }

            }
        }catch (Exception sqle){
            System.out.println("Exception: " + sqle);
        }finally {
            try { if (reportResult != null) reportResult.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};
            Menu.managerMenu();
        }
    }

}
