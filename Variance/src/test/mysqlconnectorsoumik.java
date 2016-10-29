package test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.*;
import java.util.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Formatter;
import java.text.*;


/**
 *
 * @author Soumik
 */
public class mysqlconnectorsoumik
{
    public static void main(String args[])
    {
        try
        {
            Iterator it = null; 
            Connection cn=null;
            Statement stmt = null;
            Class.forName("com.mysql.jdbc.Driver");
            cn=DriverManager.getConnection("jdbc:mysql://localhost:3306/busdata","root","root");
            stmt = cn.createStatement();
            String statement="select * from schedule_x";
            ResultSet rs=stmt.executeQuery(statement);
            int trip=1;
            ArrayList<schedule> a= new ArrayList();
            
            int count=0;
            int counter=0;
            Date d=null;
            schedule s=null;
            SimpleDateFormat sdf=null;
            while(rs.next()){
                    counter++;
                    s=new schedule();
                    s.id=rs.getInt("ID");
                    s.trip_id=trip;
                    sdf = new SimpleDateFormat("hh:mm aa");
                    //System.out.println(rs.getString("Time"));
                    d=sdf.parse(rs.getString("Time"));
                    s.t=new Time(d.getTime());
                    
                    s.stop=rs.getString("Stop");
                    s.route=rs.getString("Route");
                    
                    if(counter==4){
                        trip++;
                        counter=0;
                    }
                    
                    a.add(s);
            }
            String sql =null;
            String stop=null;
            System.out.println(a.size());
        for(int j=0;j<a.size();j++){
            //System.out.println(a.get(j).id+", "+a.get(j).route+", "+a.get(j).stop+", "+a.get(j).t+", "+a.get(j).trip_id);
            stop = a.get(j).stop;
            
            sql = "INSERT INTO schedule_trans_x " +
                  "VALUES ("+a.get(j).id+",'"+a.get(j).route+"','"+stop+"','"+a.get(j).t+"',"+a.get(j).trip_id+")";
      	 System.out.println(sql);
      	//break;
      	 stmt.executeUpdate(sql);
        }
                
            
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}