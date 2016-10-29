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
import java.text.SimpleDateFormat;


public class mysqlconnector 
{
    public static void main(String args[])
    {
        try
        {
            int size = 0;
            route obj = null;
            A aobj=null;
            ArrayList<A> a1list = new ArrayList<A>();
            ArrayList<A> a2list = new ArrayList<A>();
            ArrayList<A> a3list = new ArrayList<A>();
            ArrayList<A> a4list = new ArrayList<A>();
            ArrayList<A> a5list = new ArrayList<A>();
            ArrayList<A> a6list = new ArrayList<A>();
            
            ArrayList<route> arr = new ArrayList<route>();
            ArrayList<route> arrList = new ArrayList<route>();
            Iterator it = null; 
            Connection cn=null;
            Statement stmt = null;
            Class.forName("com.mysql.jdbc.Driver");
            cn=DriverManager.getConnection("jdbc:mysql://localhost:3306/busdata","root","root");
            stmt = cn.createStatement();
            String sql = "select * from x_interval where route_id='X' order by bus_id,dsttime";
            String insertSql=null;
            String sqla1 = null;
            ResultSet rs = stmt.executeQuery(sql);
            int counter=1;
            int i=0;
            
            while(rs.next()){
            	if(rs.getInt("src")==76 && rs.getString("Dest").equals("Stadium (X)"))
                {
            		if(arr.size()==3){
                		for(i=0;i<arr.size();i++){
                			arrList.add(arr.get(i));
                		}
            			counter++;
            			arr.clear();
            		}else{
            			arr.clear();
            		}
                }
                obj = new route();
                //Retrieve by column name
                obj.src  = rs.getInt("src");
                obj.time = rs.getInt("time");
                obj.bus_id = rs.getInt("bus_id");
                obj.route_id = rs.getString("route_id");
                obj.dest = rs.getString("Dest");
                obj.dsttime = rs.getTimestamp("dsttime");
                obj.trip_id = counter;
                arr.add(obj);
            }
            //Populate the arraylists for a1,a2 etc
            rs = stmt.executeQuery("select * from x1");
            while(rs.next()){
            	 aobj =new A();
            	 aobj.bus_id= rs.getInt("Bus");
            	 aobj.date = rs.getDate("Date");
            	 a1list.add(aobj);            	 
            }
            rs = stmt.executeQuery("select * from x2");
            while(rs.next()){
            	 aobj =new A();
            	 aobj.bus_id= rs.getInt("Bus");
            	 aobj.date = rs.getDate("Date");
            	 a2list.add(aobj);            	 
            }
            
            System.out.println("arrlistsize="+arrList.size());
            for(i=0;i<arrList.size();i++)
            {
    			if(compareDateAndBusId(arrList.get(i).dsttime ,arrList.get(i).bus_id , a1list)){ //
            		arrList.get(i).route_id = "x1";
            	}
    			else if(compareDateAndBusId(arrList.get(i).dsttime ,arrList.get(i).bus_id , a2list)){ //
            		arrList.get(i).route_id = "x2";
            	}
    			else{
    				arrList.remove(i);
    			}
    		}
            
            System.out.println("arrlistsize="+arrList.size());
            int counter2 =0;
//            for(i=0;i<arrList.size();i++){
//            	if(arrList.get(i).route_id.equals("A"))
//            	{
//            		arrList.remove(i);
//            	}

           
            System.out.println("arstsize="+arrList.size());
            String  dest =null;
            Time arrivalTime=null;
            int time=0;
            for(i=0;i<arrList.size();i++){
            	System.out.println(arrList.get(i).trip_id+", "+arrList.get(i).route_id+", "+arrList.get(i).bus_id+", "+arrList.get(i).src+", "+arrList.get(i).dest+", "+arrList.get(i).dsttime+", "+arrList.get(i).time);
    			//@todo insert this recordinto a table
            	dest = arrList.get(i).dest;
            	if(arrList.get(i).src==76){
            		time=arrList.get(i).time;
                	time=time*1000;
                	arrivalTime = new Time(arrList.get(i).dsttime.getTime()-time);   
            		sql = "INSERT INTO intervaltransformed " +
                            "VALUES ("+arrList.get(i).trip_id+",'"+arrList.get(i).route_id+"',"+arrList.get(i).bus_id+","+arrList.get(i).src+",'"+"load"+"','"+arrList.get(i).dsttime+"',"+arrList.get(i).time+",'"+arrivalTime+"'"+")";
            		stmt.executeUpdate(sql);
            		
            		dest = arrList.get(i).dest;
            		arrivalTime = new Time(arrList.get(i).dsttime.getTime());
            		sql = "INSERT INTO intervaltransformed " +
                            "VALUES ("+arrList.get(i).trip_id+",'"+arrList.get(i).route_id+"',"+arrList.get(i).bus_id+","+arrList.get(i).src+",'"+dest+"','"+arrList.get(i).dsttime+"',"+arrList.get(i).time+",'"+arrivalTime+"'"+")";
            		stmt.executeUpdate(sql);
            		
            	}else{
                	time=arrList.get(i).time;
                	time=time*1000;
                	arrivalTime = new Time(arrList.get(i).dsttime.getTime()-time);                	
                }
            			
            	sql = "INSERT INTO intervaltransformed " +
                        "VALUES ("+arrList.get(i).trip_id+",'"+arrList.get(i).route_id+"',"+arrList.get(i).bus_id+","+arrList.get(i).src+",'"+dest+"','"+arrList.get(i).dsttime+"',"+arrList.get(i).time+",'"+arrivalTime+"'"+")";
            	 System.out.println(sql);
            	 stmt.executeUpdate(sql);
           //break;
    			
            }
           
            //trip_id,routeid,stadium_out,stadium_in,wells,3rd_jordan,IMU
        }
        catch(Exception e)
        {
                e.printStackTrace();
        }
    } //endofmain
    public  static boolean compareDateAndBusId(Timestamp dsttime,int bus_id,ArrayList<A> a1list)
    {
    	boolean flag=false;
    	int i=0;
    	for(i=0;i<a1list.size();i++)
    	{
    		if(a1list.get(i).bus_id == bus_id && compareDate(dsttime,a1list.get(i).date)){
    			flag=true;
    			break;
    		}
    	} 
    	return flag;
	}//endofcompareDateAndBusId()
    public  static boolean compareDate(Timestamp dsttime, Date adate)
    {
    	boolean flag=false;
    	String dstdate = new SimpleDateFormat("MM/dd/yyyy").format(dsttime);
    	String adateformatted = new SimpleDateFormat("MM/dd/yyyy").format(adate);
    	flag = dstdate.equals(adateformatted);
    	return flag;
    	
    }//endofcompareDate()
}