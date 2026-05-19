/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DAO {
    public static Connection con;

    public DAO() {
        if (con == null) {
            String dbUrl = "jdbc:mysql://localhost:3306/cnpm?useUnicode=true&characterEncoding=UTF-8";
            String dbClass = "com.mysql.cj.jdbc.Driver";

            try {
                Class.forName(dbClass);
                con = DriverManager.getConnection(dbUrl, "root", "a0withlove1620@@");
                System.out.println("Kết nối database thành công!");
            } catch (Exception e) {
                System.out.println("Kết nối database thất bại!");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new DAO();
    }
}
