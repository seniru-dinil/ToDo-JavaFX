package dbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
   private static DataBaseConnection instance;
   private Connection dbc;

   private DataBaseConnection() throws SQLException {
       String url ="jdbc:mysql://localhost:3306/TodoListDB";
       String username = "root";
       String pw ="1234";
       dbc = DriverManager.getConnection(url,username,pw);
   }

   public static DataBaseConnection getInstance() throws SQLException {
       if(instance==null) instance=new DataBaseConnection();
       return instance;
   }

   public Connection getConnection(){
       return dbc;
   }

}
