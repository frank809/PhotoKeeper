package com.frankapp;

import org.sqlite.SQLiteException;

import java.io.IOError;
import java.sql.*;

/**
 * Created by j34liu on 2016/11/3.
 */
public class dbKeeper {

    Connection connection = null;

    Statement statement = null;

    public  dbKeeper(){
        try
        {
            // create a database connection
            this.connection = DriverManager.getConnection("jdbc:sqlite:target\\photos.db");
            this.statement = this.connection.createStatement();
            this.statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists sha");
            statement.executeUpdate("create table sha (id string , name string, path string, PRIMARY KEY('id'))");

        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        finally
        {
//            try
//            {
//                if(connection != null)
//                    connection.close();
//            }
//            catch(SQLException e)
//            {
//                // connection close failed.
//                System.err.println(e);
//            }
        }

    }

    public boolean saveSHA2db(String sha,String date,String path){

        try{
//            this.statement.executeUpdate("insert into person values('aha', 'leo')");
//            this.statement.executeUpdate("insert into person values('2', 'yui')");
//            this.statement.executeUpdate("insert into person values('23','liujiand')");
//            ResultSet rs = statement.executeQuery("select count(*) as id from person where name = 'liujiand'");
            this.statement.executeUpdate("insert into sha values('"+sha+"','"+date+"','"+path+"')");
//            ResultSet rs = this.statement.executeQuery("select * from person");
//            while(rs.next())
//            {
//                // read the result set
//                System.out.println("id = " + rs.getString("id")+" name = " + rs.getString("name"));
////                System.out.println("id = " + rs.getInt("id"));
//            }
        }catch(SQLException e){
            if (e.getMessage().toUpperCase().startsWith("[SQLITE_CONSTRAINT_PRIMARYKEY]")){
                System.out.println("SHA:["+sha+"] is already exist.");
                return false;
            }else {
                e.printStackTrace();
            }
        }

        return true;
    }
}
