package com.library;

import com.library.connection.JDBC;

import javax.swing.*;
import java.sql.*;

public class Bibliothequaire {
    private static final Connection connection = JDBC.main();

    public static ResultSet checkUsership (String usership) throws SQLException {
        String query = "SELECT id FROM bibliothequaire WHERE `usership` = ? ";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,usership);

        return preparedStatement.executeQuery();
    }

    public static boolean authentification(){
        String usership = null;
        int id = -1;

        try {
            while (usership == null || usership.trim().isEmpty()){
                usership = JOptionPane.showInputDialog(null,"Veuillez enter votre Usership :");
            }
            String query = "SELECT * FROM `bibliothequaire` WHERE usership = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,usership);
            ResultSet Return = preparedStatement.executeQuery();
            while(Return.next()){
                id = Return.getInt(1);
            }
            if (id>0){
                return true;
            }else{
                JOptionPane.showMessageDialog(null,"le usership est incorrecte");
                return false;
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
