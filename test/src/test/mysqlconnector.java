package test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.*;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Formatter;
import java.text.*;


/**
 *
 * @author Soumik
 */
public class mysqlconnector 
{
    public static void main(String args[])
    {   
        try
        {
            Iterator it = null; 
            Connection cn=null;
            Statement stmt = null;
            Class.forName("com.mysql.jdbc.Driver");
            cn=DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","evilkai4");
            stmt = cn.createStatement();
            
            String statement="select distinct d1 from joinx order by d1";
            String s1="select avg(variance) as var,d1,Total,SumOfInbound,SumOfOutbound from (select date(a.dsttime) as d1,a.variance,i.Total,i.SumOfInbound,i.SumOfOutbound from x_intervaltransformed a,qrytotalsx i where date(dsttime)='";
            String s2="'and date(i.Date)='";
            String s3="') as t"; 
            ResultSet rs=stmt.executeQuery(statement);
            
            int trip=1;
            String b;
            ArrayList<apassenger> a= new ArrayList();
            while(rs.next())
            {   
                apassenger a1=new apassenger();
                Date t1=rs.getDate("d1");
                System.out.println(t1);
                Statement stmt1= cn.createStatement();
                ResultSet rs1=stmt1.executeQuery(s1+t1.toString()+s2+t1.toString()+s3);
                while(rs1.next())
                {
                System.out.println(s1+t1.toString()+s2+t1.toString()+s3);
                System.out.println(stmt1);
                System.out.println("Rs1.d1="+rs1);
                System.out.println("a1.d1"+a1.d1);
                a1.d1=rs1.getDate("d1");
                a1.total=rs1.getDouble("Total");
                a1.inbound=rs1.getDouble("SumOfInbound");
                a1.outbound=rs1.getDouble("SumOfOutbound");
                a1.var=rs1.getFloat("var");
                a.add(a1);
                }
            }
            String sql=null;
         for(int i=0;i<a.size();i++){
             sql = "INSERT INTO s4 VALUES ("+a.get(i).var+",'"+a.get(i).d1+"',"+a.get(i).total+","+a.get(i).inbound+","+a.get(i).outbound+")";
            	 System.out.println(sql);
            	stmt.executeUpdate(sql);
             System.out.println(a.get(i).d1.toString()+" "+a.get(i).var+" "+a.get(i).inbound+" "+a.get(i).outbound+" "+a.get(i).total);
         }
          
            
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}