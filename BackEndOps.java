package com.company;

import java.sql.*;

public class BackEndOps {

    static void saleCommission(String username){

        Connection connection = null;
        ResultSet commissionQuery = null;
        Statement statement = null;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();

            {
                float commission;
                PreparedStatement pstmt = connection.prepareStatement("select title, basket.price, commission, publisher from book inner join basket on book.title = basket.book_title and username = ?");
                pstmt.setString(1, username);
                commissionQuery = pstmt.executeQuery();

                if (commissionQuery.next()) {
                    commission = commissionQuery.getFloat(2) * commissionQuery.getFloat(3);
                    PreparedStatement pstmt2 = connection.prepareStatement("update publisher set bank_acc = bank_acc+? where name = ?;");
                    pstmt2.setFloat(1, commission);
                    pstmt2.setString(2, commissionQuery.getString(4));
                    int commissionApplied = pstmt2.executeUpdate();

                    if (commissionApplied < 1) {
                        System.out.println("Could not apply commission to publisher " + commissionQuery.getString(4));
                    }

                } else {
                    System.out.println("Could not find publisher.");
                }
            }
        } catch (Exception sqle){
            System.out.println("Exception: " + sqle);
        } finally {
            try { if (commissionQuery != null) commissionQuery.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};


        }

    }

    static void recordSale(String username, int bookID, String title, String genre, String author, String publisher, int quantity, float price){

        Connection connection = null;
        ResultSet saleCount = null;
        Statement statement = null;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();

            {
                int saleID;
                PreparedStatement pstmt = connection.prepareStatement("select count (*) from sale;");
                saleCount = pstmt.executeQuery();
                if (!saleCount.next()) {
                    saleID = 1;
                } else {
                    saleID = saleCount.getInt(1) + 1;
                }

                PreparedStatement pstmt2 = connection.prepareStatement("insert into sale (sale_id, book_id, book_title, book_genre, book_author, book_publisher, quantity, sold_for)" +
                        " values (?, ?, ?, ?, ?, ?, ?, ?)");
                pstmt2.setInt(1, saleID);
                pstmt2.setInt(2, bookID);
                pstmt2.setString(3, title);
                pstmt2.setString(4, genre);
                pstmt2.setString(5, author);
                pstmt2.setString(6, publisher);
                pstmt2.setInt(7, quantity);
                pstmt2.setFloat(8, price);

                int saleRecorded = pstmt2.executeUpdate();

                if (saleRecorded < 1) {
                    System.out.println("Could not record the sale in our system.");
                    return;
                }

            }
        }catch(Exception sqle){
            System.out.println("Exception: " + sqle);
        } finally {
            try { if (saleCount != null) saleCount.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};
        }
    }

}
