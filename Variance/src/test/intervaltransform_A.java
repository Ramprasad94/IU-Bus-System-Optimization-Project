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

class intervaltransform_A
{
    public static void main(String args[])
    {
        try
        {
            Iterator it = null; 
            Connection cn=null;
            Statement stmt = null;
            Statement stmt2 = null;
            Class.forName("com.mysql.jdbc.Driver");
            cn=DriverManager.getConnection("jdbc:mysql://localhost:3306/busdata","root","root");
            stmt = cn.createStatement();
            stmt2 = cn.createStatement();
            String statement="select * from intervaltransformed";
            ResultSet rs=stmt.executeQuery(statement);
            ArrayList<schedule> a= new ArrayList();
            SimpleDateFormat sdf=null;
            long time=0;
            Time arrivalTime=null;
            Timestamp dsttime=null;
            long dst=0;
            int tripId=0;
            int src=0;
            String dest=null;
            int busId=0;
            String routeId=null;
            String sql=null;
            int counter=0;
            while(rs.next()){
            	tripId=rs.getInt("tripId");
            	src=rs.getInt("src");
            	busId=rs.getInt("busId");
            	routeId=rs.getString("routeId");
            	dsttime = rs.getTimestamp("dsttime");
            	time =rs.getLong("time"); 
            	dest=rs.getString("dest");
            	if(src==24){
            		arrivalTime=new Time(dsttime.getTime());
            	}else{
            		arrivalTime=new Time(dsttime.getTime()-(time*1000));
            	}
            	
            	
               sql = "INSERT INTO b_semiintervaltransformed " +
                       "VALUES ("+tripId+",'"+routeId+"',"+busId+","+src+",'"+dest+"','"+dsttime+"',"+time+",'"+arrivalTime+"')";
             	
               System.out.println("counter="+counter);
               //System.out.println(sql);
              //	break;
           	   stmt2.executeUpdate(sql);
           //	counter++;
            
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}