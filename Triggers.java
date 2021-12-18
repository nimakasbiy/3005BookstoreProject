package com.company;

import java.sql.*;
import java.util.Random;

public class Triggers {

    static void stockOrderTrigger(String bookTitle){

        Connection connection = null;
        Statement statement = null;
        ResultSet quantityResult = null;
        int newStock;
        int refillStock;

        try {connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/3005final",
                "postgres", "password");
            statement = connection.createStatement();
            {
                PreparedStatement pstmt = connection.prepareStatement("select quantity from book where title = ?");
                pstmt.setString(1, bookTitle);
                quantityResult = pstmt.executeQuery();

                if (quantityResult.next()){
                    if (quantityResult.getInt(1) < 10){
                        Random r = new Random();
                        newStock = r.nextInt((99-10)+1) + 10;

                        PreparedStatement pstmt2 = connection.prepareStatement("update book set quantity = quantity + ? where title = ?;");
                        pstmt2.setInt(1, newStock);
                        pstmt2.setString(2, bookTitle);

                        refillStock = pstmt2.executeUpdate();

                        if (refillStock == 1){
                            System.out.println("NEW STOCK: The store has received " + newStock + " more units of " + bookTitle);
                        }
                    }
                }

            }
        }catch (Exception sqle){
            System.out.println("Exception: " + sqle);
        }finally {
            try { if (quantityResult != null) quantityResult.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (connection != null) connection.close(); } catch (Exception e) {};
        }
    }

}
