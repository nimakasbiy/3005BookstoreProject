package com.company;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Customer {

    static boolean login(String username, String password){

        boolean loggedIn = false;

        Connection connection = null;
        ResultSet loginResult = null;
        Statement statement = null;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();
            {
                PreparedStatement pstmt = connection.prepareStatement("select * from customer where username = ? and password = ?;");
                pstmt.setString(1, username);
                pstmt.setString(2, password);

                loginResult = pstmt.executeQuery();

                if (loginResult.next()) {
                    System.out.println("Welcome, " + username);
                    loggedIn = true;
                } else {
                    loggedIn = false;
                }
            }
        }catch(Exception sqle){
            System.out.println("Exception: " + sqle);
        }finally {
            try { if (loginResult != null) loginResult.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};
        }
        return loggedIn;
    }

    static boolean register(String username, String password, String card_no, String billingAddress, String shippingAddress){

        boolean registered = false;

        Connection connection = null;
        ResultSet registerCheck = null;
        Statement statement = null;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();
            {

                PreparedStatement pstmt = connection.prepareStatement("select count (*) from customer;");

                registerCheck = pstmt.executeQuery();
                if (registerCheck.next()) {
                    PreparedStatement pstmt2 = connection.prepareStatement("insert into customer " +
                            "(username, password, card_no, billing_addr, shipping_addr) " +
                            "values (?, ?, ?, ?, ?);");
                    pstmt2.setString(1, username);
                    pstmt2.setString(2, password);
                    pstmt2.setString(3, card_no);
                    pstmt2.setString(4, billingAddress);
                    pstmt2.setString(5, shippingAddress);

                    int registerResult = pstmt2.executeUpdate();

                    if (registerResult == 1) {
                        registered = true;
                    }
                }
            }
        }catch(Exception sqle){
            System.out.println("Exception: " + sqle);
        }finally {
            try { if (registerCheck != null) registerCheck.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};
        }

        if (registered){
            Menu.searchMenu(username);
        }
        else {
            Menu.welcomeMenu();
        }
        return registered;
    }

    static void addToBasket(String username){

        Connection connection = null;
        ResultSet bookResult = null;
        Statement statement = null;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();
            {
                int bookID;
                int quantity;

                System.out.println("Book ID: ");
                Scanner bookNum_scn = new Scanner(System.in);
                bookID = bookNum_scn.nextInt();

                System.out.println("Quantity: ");
                Scanner quantity_scn = new Scanner(System.in);
                quantity = quantity_scn.nextInt();

                PreparedStatement pstmt1 = connection.prepareStatement("select * from book where ID = ?");
                pstmt1.setInt(1, bookID);

                bookResult = pstmt1.executeQuery();

                while (!bookResult.next()) {
                    System.out.println("Book ID invalid, try again.");
                    System.out.println("Book ID: ");
                    bookID = bookNum_scn.nextInt();

                    System.out.println("Quantity: ");
                    quantity = quantity_scn.nextInt();

                    pstmt1.setInt(1, bookID);
                    bookResult = pstmt1.executeQuery();
                }

                while (bookResult.getInt(9) < quantity) {
                    System.out.println("Not enough books in stock. Try ordering less, or choose another book.");
                    System.out.println("Book ID: ");
                    bookID = bookNum_scn.nextInt();

                    System.out.println("Quantity: ");
                    quantity = quantity_scn.nextInt();
                }

                float totalPrice = bookResult.getFloat(6) * quantity;
                Math.round(totalPrice * 100);

                PreparedStatement pstmt2 = connection.prepareStatement("update book set quantity = quantity-? where ID = ?;");
                pstmt2.setInt(1, quantity);
                pstmt2.setInt(2, bookID);

                int quanityUpdated = pstmt2.executeUpdate();

                if (quanityUpdated == 1) {
                    PreparedStatement pstmt3 = connection.prepareStatement("insert into basket (username, book_id, book_title, price, quantity) values (?, ?, ?, ?, ?)");
                    pstmt3.setString(1, username);
                    pstmt3.setInt(2, bookID);
                    pstmt3.setString(3, bookResult.getString(3));
                    pstmt3.setFloat(4, totalPrice);
                    pstmt3.setInt(5, quantity);

                    int addedToBasket = pstmt3.executeUpdate();

                    if (addedToBasket == 1) {
                        System.out.println("Book with ID " + bookID + " and Quantity " + quantity + " added to basket (Price: $" + totalPrice + ")");
                        return;
                    } else {
                        System.out.println("Could not add to basket.");
                        return;
                    }
                } else {
                    System.out.println("Could not update stock");
                    return;
                }

            }
        }catch(Exception sqle){
            System.out.println("Exception: " +sqle);
        } finally {
            try { if (bookResult != null) bookResult.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};
            Menu.searchMenu(username);
        }
    }

    static void viewBasket(String username){

        Connection connection = null;
        ResultSet basketResult = null;
        Statement statement = null;

        boolean returnToSearch = true;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();

            {
                String order;

                System.out.println("YOUR BASKET");
                PreparedStatement pstmt = connection.prepareStatement("select book_id, book_title, quantity, price, 'genre', 'author', 'publisher' from basket where username = ?");
                pstmt.setString(1, username);

                basketResult = pstmt.executeQuery();

                if (!basketResult.next()) {
                    System.out.println("Nothing in your basket... yet.");
                    return;
                } else {
                    String headerString = String.format("|%-5s|%-50s|%-10s|%-10s|", "ID", "TITLE", "QUANTITY", "PRICE");
                    System.out.println(headerString);
                    do {
                        int bookID = basketResult.getInt(1);
                        String title = basketResult.getString(2);
                        int quantity = basketResult.getInt(3);
                        float price = basketResult.getFloat(4);
                        String resultsString = String.format(" %-5s %-50s %-10s %-10s ", bookID, title, quantity, price);
                        System.out.println(resultsString);
                    } while (basketResult.next());
                }
                returnToSearch = false;
                return;
            }
        } catch (Exception sqle){
            System.out.println("Exception: " + sqle);
        } finally {
            try { if (basketResult != null) basketResult.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};

            if (returnToSearch) {Menu.searchMenu(username);}
            else {makeOrder(username);}
        }
    }

    static void makeOrder(String username){

        Connection connection = null;
        ResultSet orderCount = null;
        ResultSet destination = null;
        ResultSet basketToBook = null;
        Statement statement = null;

        boolean returnToSearch = true;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();

            {
                String order;
                int orderNum = -1;
                int orderConfirmed;
                int basketCleared;
                String date_ordered = new SimpleDateFormat("MM-dd-yyyy").format(new Date());

                System.out.println("Complete order? (y/n)");
                Scanner order_scn = new Scanner(System.in);
                order = order_scn.next();

                PreparedStatement pstmt = connection.prepareStatement("select count (*) from orders;");
                orderCount = pstmt.executeQuery();
                if (!orderCount.next()) {
                    orderNum = 1;
                } else {
                    orderNum = orderCount.getInt(1) + 1;
                }

                if (order.equals("Y") || order.equals("y")) {
                    PreparedStatement pstmt2 = connection.prepareStatement("select shipping_addr from customer where username = ?");
                    pstmt2.setString(1, username);
                    destination = pstmt2.executeQuery();

                    if (!destination.next()) {
                        System.out.println("Your shipping address was not found");
                        //searchMenu(username);
                        return;
                    } else {
                        PreparedStatement pstmt3 = connection.prepareStatement("insert into orders (order_number, username, origin, destination, date_ordered, status)" +
                                "values (?, ?, ?, ?, ?, ?);");
                        pstmt3.setInt(1, orderNum);
                        pstmt3.setString(2, username);
                        pstmt3.setString(3, "BookDB Ottawa Warehouse");
                        pstmt3.setString(4, destination.getString(1));
                        pstmt3.setString(5, date_ordered);
                        pstmt3.setString(6, "Order Shipped");

                        orderConfirmed = pstmt3.executeUpdate();

                        if (orderConfirmed == 1) {
                            PreparedStatement pstmt4 = connection.prepareStatement("select book_id, book_title, genre, author, publisher, basket.quantity, basket.price from basket inner join book " +
                                    " on username = ? and book.title = book_title;");
                            pstmt4.setString(1, username);

                            basketToBook = pstmt4.executeQuery();

                            if (!basketToBook.next()) {
                                System.out.println("Could not connect your basket to our book collection.");
                                return;
                            }

                            BackEndOps.saleCommission(username);

                            do {
                                BackEndOps.recordSale(username, basketToBook.getInt(1), basketToBook.getString(2),
                                        basketToBook.getString(3), basketToBook.getString(4),
                                        basketToBook.getString(5), basketToBook.getInt(6),
                                        basketToBook.getFloat(7));

                                Triggers.stockOrderTrigger(basketToBook.getString(2));
                            } while (basketToBook.next());

                            PreparedStatement pstmt6 = connection.prepareStatement("delete from basket where username = ?;");
                            pstmt6.setString(1, username);

                            basketCleared = pstmt6.executeUpdate();

                            if (basketCleared < 1) {
                                System.out.println("Basket could not be cleared.");
                                return;
                            } else {
                                System.out.println("Your order has been completed! Your Order # is: " + orderNum);
                                return;
                            }
                        } else {
                            System.out.println("Order could not be completed.");
                            return;
                        }
                    }
                } else {
                    return;
                }
            }
        } catch (Exception sqle){
            System.out.println("Exception: " + sqle);
        }finally {
            try { if (orderCount != null) orderCount.close(); } catch (Exception e) {};
            try { if (destination != null) destination.close(); } catch (Exception e) {};
            try { if (basketToBook != null) basketToBook.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};

            if (returnToSearch) {Menu.searchMenu(username);}
        }
    }

    static void trackOrder(int orderNumber, String username){

        Connection connection = null;
        Statement statement = null;
        ResultSet orderResult = null;

        boolean returnToSearch = true;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();

            {
                PreparedStatement pstmt = connection.prepareStatement("select order_number, username, date_ordered, origin, destination, status from orders where order_number = ? and username = ?;");
                pstmt.setInt(1, orderNumber);
                pstmt.setString(2, username);

                orderResult = pstmt.executeQuery();

                if (!orderResult.next()) {
                    System.out.println("Order #" + orderNumber + " does not exist under your account.");
                    //searchMenu(username);
                    return;
                } else {
                    System.out.println("YOUR ORDER");
                    do {
                        String headerString = String.format("|%-10s|%-20s|%-30s|%-30s|%-15s|", "ORDER #", "DATE OF ORDER", "ORIGIN", "DESTINATION", "STATUS");
                        System.out.println(headerString);
                        int orderNum = orderResult.getInt(1);
                        String orderDate = orderResult.getString(3);
                        String orderOrigin = orderResult.getString(4);
                        String orderDestination = orderResult.getString(5);
                        String orderStatus = orderResult.getString(6);
                        String resultString = String.format(" %-10d %-20s %-30s %-30s %-15s", orderNum, orderDate, orderOrigin, orderDestination, orderStatus);
                        System.out.println(resultString);
                    } while (orderResult.next());
                }
            }
        } catch (Exception sqle){
            System.out.println("Exception: " + sqle);
        } finally {
            try { if (orderResult != null) orderResult.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};

            if (returnToSearch) {Menu.searchMenu(username);}
        }
    }



}
