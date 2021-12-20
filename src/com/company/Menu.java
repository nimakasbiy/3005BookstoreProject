package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

public class Menu {

    /* Function that displays the menu for a customer
    * and prompts the user for a valid choice */
    static void searchMenu(String username){
        System.out.println("\nBrowse our collection!");
        System.out.println( "(1) All our books\n" +
                "(2) Search by Title\n" +
                "(3) Search by Author\n" +
                "(4) Search by Genre\n\n" +

                "Other things..\n" +
                "(5) Your basket & Make Orders\n" +
                "(6) Track an order\n" +
                "(7) Log Out");

        int searchChoice = -1;

        while (searchChoice < 1 || searchChoice > 7){
            System.out.println("Enter your choice:");
            Scanner search_scn = new Scanner(System.in);
            searchChoice = search_scn.nextInt();
        }

        if (searchChoice == 1){
            Search.searchAllBooks(username);
            String orderChoice;
            System.out.println("Add a book to basket? (y/n)");
            Scanner order_scn = new Scanner(System.in);
            orderChoice = order_scn.next();

            if (orderChoice.equals("y") || orderChoice.equals("Y")){
                Customer.addToBasket(username);
            }
            else{
                searchMenu(username);
            }
        }
        else if(searchChoice == 2){
            Search.searchByTitle(username);
            String orderChoice;
            System.out.println("Add a book to basket? (y/n)");
            Scanner order_scn = new Scanner(System.in);
            orderChoice = order_scn.next();

            if (orderChoice.equals("y") || orderChoice.equals("Y")){
                Customer.addToBasket(username);
            }
            else{
                searchMenu(username);
            }
        }
        else if(searchChoice == 3){
            Search.searchByAuthor(username);
            String orderChoice;
            System.out.println("Add a book to basket? (y/n)");
            Scanner order_scn = new Scanner(System.in);
            orderChoice = order_scn.next();
            if (orderChoice.equals("y") || orderChoice.equals("Y")){
                Customer.addToBasket(username);
            }
            else{
                searchMenu(username);
            }
        }
        else if (searchChoice == 4){
            Search.searchByGenre(username);
            String orderChoice;
            System.out.println("Add a book to basket? (y/n)");
            Scanner order_scn = new Scanner(System.in);
            orderChoice = order_scn.next();
            if (orderChoice.equals("y") || orderChoice.equals("Y")){
                Customer.addToBasket(username);
            }
            else{
                searchMenu(username);
            }
        }
        else if (searchChoice == 5){
            Customer.viewBasket(username);
        }
        else if(searchChoice == 6){
            int orderNumber;
            System.out.println("Enter order number: ");
            Scanner orderNum_scn = new Scanner(System.in);
            orderNumber = orderNum_scn.nextInt();
            Customer.trackOrder(orderNumber, username);
        }
        else if (searchChoice == 7){
            welcomeMenu();
        }
    }

    /* Function that displays the menu for a manager/bookstore owner
     * and prompts the user for a valid choice */
    static void managerMenu(){
        System.out.println("\nManager Mode:");
        System.out.println( "(1) All our books\n" +
                "(2) Search by Title\n" +
                "(3) Search by Author\n" +
                "(4) Search by Genre\n\n" +

                "Administrative actions:\n" +
                "(5) Add a Book\n" +
                "(6) Remove a Book\n" +
                "(7) REPORT: Best Selling Books\n" +
                "(8) REPORT: Sales per Genre\n" +
                "(9) REPORT: Sales per Author\n" +
                "(10) REPORT: Publisher Commission Report\n" +
                "(0) Log Out\n");

        int managerChoice = -1;
        while (managerChoice < 0 || managerChoice > 10){
            System.out.println("Enter your choice:");
            Scanner search_scn = new Scanner(System.in);
            managerChoice = search_scn.nextInt();
        }
        if (managerChoice == 0) {welcomeMenu();}
        else if (managerChoice == 1) {Search.searchAllBooks("MANAGER"); managerMenu();}
        else if (managerChoice == 2){Search.searchByTitle("MANAGER"); managerMenu();}
        else if (managerChoice == 3){Search.searchByAuthor("MANAGER"); managerMenu();}
        else if (managerChoice == 4){Search.searchByGenre("MANAGER"); managerMenu();}
        else if (managerChoice == 5){Manager.addBook();}
        else if (managerChoice == 6){Manager.removeBook();}
        else if (managerChoice == 7){Manager.bestSellersReport();}
        else if (managerChoice == 8){Manager.salesPerGenreReport();}
        else if (managerChoice == 9){Manager.salesPerAuthorReport();}
        else if (managerChoice == 10){Manager.commissionReport();}
    }

    /* Function that displays the initial welcome menu
     * and prompts the user for a valid choice */
    static void welcomeMenu(){
        String currUser = null;

        Connection connection = null;
        Statement statement = null;

        boolean goToSearchMenu = false;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();

            {
                System.out.println("Welcome to bookDB...");
                System.out.println("(1) Log in     (2) Register     (3) Manager mode     (4) Exit");

                int menuChoice = -1;


                while (menuChoice < 1 || menuChoice > 4) {
                    System.out.println("Enter your choice: ");
                    Scanner scn1 = new Scanner(System.in);
                    menuChoice = scn1.nextInt();
                }

                if (menuChoice == 1) {
                    System.out.println("Username: ");
                    Scanner username_scn = new Scanner(System.in);
                    String username = username_scn.next();

                    System.out.println("Password: ");
                    Scanner password_scn = new Scanner(System.in);
                    String password = password_scn.next();


                    while (!Customer.login(username, password)) {
                        System.out.println("Incorrect login credentials, please try again or register.");
                        System.out.println("Username: ");
                        username = username_scn.next();

                        System.out.println("Password: ");
                        password = password_scn.next();

                    }
                    currUser = username;
                    goToSearchMenu = true;

                } else if (menuChoice == 2) {

                    System.out.println("Username: ");
                    Scanner username_scn = new Scanner(System.in);
                    String username = username_scn.next();

                    System.out.println("Password: ");
                    Scanner password_scn = new Scanner(System.in);
                    String password = password_scn.next();

                    System.out.println("Credit/Debit Card Number: ");
                    Scanner card_scn = new Scanner(System.in);
                    String card = card_scn.next();

                    System.out.println("Billing Address: ");
                    Scanner billingaddr_scn = new Scanner(System.in);
                    String billingAddr = billingaddr_scn.nextLine();

                    System.out.println("Shipping Address: ");
                    Scanner shippingAddr_scn = new Scanner(System.in);
                    String shippingAddr = shippingAddr_scn.nextLine();

                    if (Customer.register(username, password, card, billingAddr, shippingAddr)) {
                        goToSearchMenu = true;
                        currUser = username;
                    }
                } else if (menuChoice == 3){
                    managerMenu();
                } else if (menuChoice == 4){
                    System.exit(0);
                }
            }
        } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
        } finally {
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};
            if (goToSearchMenu) {searchMenu(currUser);}
        }
    }

}
