package com.company;

import java.sql.*;
import java.util.Scanner;

public class Search {

    static void searchAllBooks(String username){

        Connection connection = null;
        Statement statement = null;
        ResultSet searchResult = null;

        boolean returnToSearch = false;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();
            {
                PreparedStatement pstmt = connection.prepareStatement("select id, isbn, title, author, genre, price, publisher, quantity from book order by title;");
                searchResult = pstmt.executeQuery();

                if (!searchResult.next()) {
                    System.out.println("Sorry! No books found.");
                    returnToSearch = true;
                } else {
                    String headerString = String.format("|%-5s|%-15s|%-50s|%-30s|%-15s|%-10s|%-10s|%-30s", "ID", "ISBN", "Title", "Author", "Genre", "Price", "In Stock", "Publisher");
                    System.out.println(headerString);
                    do {
                        String isbn = searchResult.getString(2);
                        String title = searchResult.getString(3);
                        String author = searchResult.getString(4);
                        String genre = searchResult.getString(5);
                        float price = searchResult.getFloat(6);
                        String publisher = searchResult.getString(7);
                        int bookID = searchResult.getInt(1);
                        int quantity = searchResult.getInt(8);
                        String resultString = String.format(" %-5d %-15s %-50s %-30s %-15s %-10s %-10d %-30s", bookID, isbn, title, author, genre, price, quantity, publisher);
                        System.out.println(resultString);
                    } while (searchResult.next());
                }
            }
        }catch(Exception sqle){
            System.out.println("Exception: " + sqle);
        }finally {
            try { if (searchResult != null) searchResult.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};
            if (returnToSearch) {
                if (username.equals("MANAGER")){
                   Menu.managerMenu();
                }
                else {
                    Menu.searchMenu(username);
                }
            }
        }

    }

    static void searchByTitle(String username){
        Connection connection = null;
        Statement statement = null;
        ResultSet searchResult = null;

        boolean returnToSearch = false;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();
            {
                String titleToSearch;
                System.out.println("Title: ");
                Scanner title_scn = new Scanner(System.in);
                titleToSearch = title_scn.nextLine();

                PreparedStatement pstmt = connection.prepareStatement("select * from book where lower(title) like lower(?);");
                pstmt.setString(1, "%" + titleToSearch + "%");
                searchResult = pstmt.executeQuery();

                if (!searchResult.next()){
                    System.out.println("No books found with Title containing: " + titleToSearch);
                    returnToSearch = true;
                }
                else {
                    String headerString = String.format("|%-5s|%-15s|%-50s|%-30s|%-15s|%-10s|%-10s|%-30s", "ID", "ISBN", "Title", "Author", "Genre", "Price", "In Stock", "Publisher");
                    System.out.println(headerString);
                    do {
                        String isbn = searchResult.getString(2);
                        String title = searchResult.getString(3);
                        String author = searchResult.getString(4);
                        String genre = searchResult.getString(5);
                        float price = searchResult.getFloat(6);
                        String publisher = searchResult.getString(7);
                        int bookID = searchResult.getInt(1);
                        int quantity = searchResult.getInt(9);
                        String resultString = String.format(" %-5d %-15s %-50s %-30s %-15s %-10s %-10d %-30s", bookID, isbn, title, author, genre, price, quantity, publisher);
                        System.out.println(resultString);
                    } while (searchResult.next());
                }
            }
        }catch (Exception sqle){
            System.out.println("Exception: " + sqle);
        }finally {
            try { if (searchResult != null) searchResult.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};
            if (returnToSearch) {
                if (username.equals("MANAGER")){
                    Menu.managerMenu();
                }
                else {
                    Menu.searchMenu(username);
                }
            }
        }
    }

    static void searchByAuthor(String username){
        Connection connection = null;
        Statement statement = null;
        ResultSet searchResult = null;

        boolean returnToSearch = false;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();
            {
                String authorToSearch;
                System.out.println("Author: ");
                Scanner title_scn = new Scanner(System.in);
                authorToSearch = title_scn.nextLine();

                PreparedStatement pstmt = connection.prepareStatement("select * from book where lower(author) like lower(?);");
                pstmt.setString(1, "%" + authorToSearch + "%");
                searchResult = pstmt.executeQuery();

                if (!searchResult.next()){
                    System.out.println("No books found with Author containing: " + authorToSearch);
                    //searchMenu(username);
                    returnToSearch = true;
                }
                else {
                    String headerString = String.format("|%-5s|%-15s|%-50s|%-30s|%-15s|%-10s|%-10s|%-30s", "ID", "ISBN", "Title", "Author", "Genre", "Price", "In Stock", "Publisher");
                    System.out.println(headerString);
                    do {
                        String isbn = searchResult.getString(2);
                        String title = searchResult.getString(3);
                        String author = searchResult.getString(4);
                        String genre = searchResult.getString(5);
                        float price = searchResult.getFloat(6);
                        String publisher = searchResult.getString(7);
                        int bookID = searchResult.getInt(1);
                        int quantity = searchResult.getInt(9);
                        String resultString = String.format(" %-5d %-15s %-50s %-30s %-15s %-10s %-10d %-30s", bookID, isbn, title, author, genre, price, quantity, publisher);
                        System.out.println(resultString);
                    } while (searchResult.next());
                }
            }
        }catch (Exception sqle){
            System.out.println("Exception: " + sqle);
        }finally {
            try { if (searchResult != null) searchResult.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};
            if (returnToSearch) {
                if (username.equals("MANAGER")){
                    Menu.managerMenu();
                }
                else {
                    Menu.searchMenu(username);
                }
            }
        }
    }

    static void searchByGenre(String username){
        Connection connection = null;
        Statement statement = null;
        ResultSet searchResult = null;

        boolean returnToSearch = false;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();
            {
                String genreToSearch;
                System.out.println("Genre: ");
                Scanner title_scn = new Scanner(System.in);
                genreToSearch = title_scn.nextLine();

                PreparedStatement pstmt = connection.prepareStatement("select * from book where lower(genre) like lower(?);");
                pstmt.setString(1, "%" + genreToSearch + "%");
                searchResult = pstmt.executeQuery();

                if (!searchResult.next()){
                    System.out.println("No books found with Genre containing: " + genreToSearch);
                    //searchMenu(username);
                    returnToSearch = true;
                }
                else {
                    String headerString = String.format("|%-5s|%-15s|%-50s|%-30s|%-15s|%-10s|%-10s|%-30s", "ID", "ISBN", "Title", "Author", "Genre", "Price", "In Stock", "Publisher");
                    System.out.println(headerString);
                    do {
                        String isbn = searchResult.getString(2);
                        String title = searchResult.getString(3);
                        String author = searchResult.getString(4);
                        String genre = searchResult.getString(5);
                        float price = searchResult.getFloat(6);
                        String publisher = searchResult.getString(7);
                        int bookID = searchResult.getInt(1);
                        int quantity = searchResult.getInt(9);
                        String resultString = String.format(" %-5d %-15s %-50s %-30s %-15s %-10s %-10d %-30s", bookID, isbn, title, author, genre, price, quantity, publisher);
                        System.out.println(resultString);
                    } while (searchResult.next());
                }
            }
        }catch (Exception sqle){
            System.out.println("Exception: " + sqle);
        }finally {
            try { if (searchResult != null) searchResult.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};
            if (returnToSearch) {
                if (username.equals("MANAGER")){
                    Menu.managerMenu();
                }
                else {
                    Menu.searchMenu(username);
                }
            }
        }
    }

}
