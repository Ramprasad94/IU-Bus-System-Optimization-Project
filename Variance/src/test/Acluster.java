package test;

import java.sql.*;
import java.util.ArrayList;
import java.util.*;
import java.sql.Date;
import java.text.SimpleDateFormat;


public class Acluster 
{
    public static void main(String args[])
    {
        try
        {
            int size = 0;
            schedule schdobj=null;
            route robj=null;
            ArrayList<schedule> schdlist = new ArrayList<schedule>();
            ArrayList<route> intrList = new ArrayList<route>();
            Iterator it = null; 
            Connection cn=null;
            Statement stmt1 = null;
            Statement stmt2 = null;
            Class.forName("com.mysql.jdbc.Driver");
            cn=DriverManager.getConnection("jdbc:mysql://localhost:3306/busdata","root","root");
            stmt1 = cn.createStatement();
            stmt2 = cn.createStatement();
            String schdsql = "SELECT * FROM schedule_trans_x where route = 'x2' and stop = 'Stadium'";
            String intrsql = "SELECT * FROM busdata.intervaltransformed where src=76 and dest LIKE 'Stadium (X)' and routeId = 'x2' order by tripId,arrivalTime";
            String insertSql=null;
            String sqla1 = null;
            ResultSet rs = stmt1.executeQuery(schdsql);
            int counter=1;
            int i,j=0;
            
            
            while(rs.next()){
            	schdobj = new schedule();
                //Retrieve by column name
            	schdobj.id  = rs.getInt("id");
            	schdobj.route  = rs.getString("route");
            	schdobj.stop  = rs.getString("stop");
            	schdobj.t  = rs.getTime("dt");
            	schdobj.trip_id  = rs.getInt("trip_id");
            	schdlist.add(schdobj);
            }
            System.out.println("schdlist.size=="+schdlist.size());
           
            rs = stmt2.executeQuery(intrsql);
            while(rs.next()){
            	robj = new route();
                //Retrieve by column name
            	
            	robj.trip_id = rs.getInt("tripId");
            	robj.route_id=rs.getString("routeId");
            	robj.bus_id= rs.getInt("busId");
            	robj.src=rs.getInt("src");
            	robj.dest= rs.getString("dest");
            	robj.dsttime=rs.getTimestamp("dsttime");      
            	robj.time=rs.getInt("time"); 
            	robj.arrivalTime=rs.getTime("arrivalTime");
            	intrList.add(robj);
            }
            System.out.println("intrlist.size=="+intrList.size());
            
            long mintimediff=0;
            long currtimediff=0;
            for(i=0;i<intrList.size();i++){
            	robj=intrList.get(i);
            	for(j=0;j<schdlist.size();j++){
	            	schdobj=schdlist.get(j);
	            	currtimediff = robj.arrivalTime.getTime()-schdobj.t.getTime();
	            	if(currtimediff<0)
	            		currtimediff=currtimediff * (-1);
	            	if(j==0){
	            		mintimediff = currtimediff;
	            		robj.schdtripID=schdobj.trip_id;
	            	}else{
	            		if(currtimediff < mintimediff){
	            			mintimediff = currtimediff;
	            			robj.schdtripID=schdobj.trip_id;
	            		}
	            	}
            	}
            }
            System.out.println("!!!!!!!!!!!!!!!!!!");
            
            for(i=0;i<intrList.size();i++){
            	robj=intrList.get(i);
//            	System.out.println("trip_id="+robj.trip_id);
//            	System.out.println("route_id="+robj.route_id);
//            	System.out.println("bus_id="+robj.bus_id);
//            	System.out.println("src="+robj.src);
//            	System.out.println("dest="+robj.dest);
//            	System.out.println("dsttime="+robj.dsttime);
//            	System.out.println("time="+robj.time);
//            	System.out.println("arrivalTime="+robj.arrivalTime);
//            	System.out.println("schdtripID="+robj.schdtripID);
//            	
            	intrsql="INSERT INTO x_pivot values("+robj.trip_id+","+robj.schdtripID+")";
            	stmt2.executeUpdate(intrsql);
            	System.out.println(intrsql);
            	//break;
            }
        }
        catch(Exception e)
        {
                e.printStackTrace();
        }
    } //endofmain
}