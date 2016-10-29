package test;
import java.sql.*;
import java.util.ArrayList;
import java.util.*;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class Avariance 
{
    public static void main(String args[])
    {
        try
        {
            int size = 0;
            schedule schdobj=null;
            route robj=null; 
            Apivot apobj=null;
            ArrayList<schedule> schdList = new ArrayList<schedule>();
            ArrayList<route> intrList = new ArrayList<route>();
            ArrayList<Apivot> pivotList = new ArrayList<Apivot>();
            Iterator it = null; 
            Connection cn=null;
            Statement stmt1,stmt2,stmt3,stmt4 = null;
            Class.forName("com.mysql.jdbc.Driver");
            cn=DriverManager.getConnection("jdbc:mysql://localhost:3306/busdata","root","root");
            stmt1 = cn.createStatement();
            stmt2 = cn.createStatement();
            stmt3 = cn.createStatement();
            stmt4 = cn.createStatement();
            String pivotsql="SELECT * FROM x_pivot"; 
            String schdsql = "SELECT * FROM schedule_trans_x";
            String intrsql = "SELECT * FROM busdata.intervaltransformed";
            String insertSql=null;
            ResultSet rs = stmt1.executeQuery(intrsql);
            int counter=0;
            int i,j,k=0;
            rs=stmt1.executeQuery(pivotsql);
            while(rs.next()){
            	apobj = new Apivot();
            	apobj.intrtripId= rs.getInt("intrtripId");
            	apobj.schdtripId= rs.getInt("schdtripId");
            	pivotList.add(apobj);
            }
            System.out.println("pivotlistsize="+pivotList.size());
//            for(j=0;j<pivotList.size();j++){
//            	System.out.println(pivotList.get(j).schdtripId);
//            }
            rs = stmt2.executeQuery(schdsql);
            while(rs.next()){
            	schdobj= new schedule();
            	schdobj.id  = rs.getInt("id");
            	schdobj.route  = rs.getString("route");
            	schdobj.stop  = rs.getString("stop");
            	schdobj.t  = rs.getTime("dt");
            	schdobj.trip_id  = rs.getInt("trip_id");
            	schdList.add(schdobj);
            }
            System.out.println("schdlistsize="+schdList.size());
            rs = stmt3.executeQuery(intrsql);
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
            System.out.println("intrlistsize="+intrList.size());
            String dest=null;
            int variance=0;
            for(i=0;i<intrList.size();i++){
            	robj = intrList.get(i);
            	for(j=0;j<pivotList.size();j++){
            		if(robj.trip_id==pivotList.get(j).intrtripId){
            			robj.schdtripID=pivotList.get(j).schdtripId;
            			break;
            		}
            	}
            	for(k=0;k<schdList.size();k++){
            		 if(robj.schdtripID==schdList.get(k).trip_id){
            			 robj.schdarrivalTime=schdList.get(k).t;
            			 break;
            		 }
            	}
            	variance=(int)(robj.arrivalTime.getTime()-robj.schdarrivalTime.getTime());
            	variance=variance/1000;
            	dest=robj.dest;
            	
            	insertSql = "INSERT INTO x_intervaltransformed " +
                      "VALUES ("+robj.trip_id+",'"+robj.route_id+"',"+robj.bus_id+","+robj.src+",'"+dest+"','"+robj.dsttime+"',"
            			+robj.time+",'"+robj.arrivalTime+"','"+robj.schdarrivalTime+"',"+variance+")";
            	//System.out.println(insertSql);
            	counter++;
            	stmt4.executeUpdate(insertSql);
            	System.out.println("insertcounter="+counter);
            }
             
            System.out.println("insertcounter="+counter);
              
        }
        catch(Exception e)
        {
                e.printStackTrace();
        }
    } //endofmain
}