package mypackage;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.math.*;
import java.util.*;
import java.sql.*;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.KeyValue;
// Extend HttpServlet class
public class q5 extends HttpServlet {
    Configuration config;
    HTable table;
    public void init() throws ServletException 
    {
        try
        {
                config = HBaseConfiguration.create();
                table = new HTable(config, "q5");
        }
        catch (Exception e)
        {
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set response content type
        response.setContentType("text/plain; charset=UTF-8");
	response.setCharacterEncoding("UTF-8");
        // Actual logic goes here.
        PrintWriter out = response.getWriter();
        out.println("Wolken,5534-0848-5100,0299-6830-9164");
	try 
	{
        	String useridA = request.getParameter("m");
		String useridB = request.getParameter("n");
		out.println(useridA + "\t" + useridB + "\tWINNER");
	        Get g1 = new Get(Bytes.toBytes(useridA));
                Get g2 = new Get(Bytes.toBytes(useridB));
		Result r1 = table.get(g1);
		Result r2 = table.get(g2);
		KeyValue[] values1 = r1.raw();
		KeyValue[] values2 = r2.raw();
		for (int i = 0; i < 4; i++)
		{
			byte[] score1 = values1[i].getValue();
			byte[] score2 = values2[i].getValue();
			int iScore1 = Integer.valueOf(Bytes.toString(score1));
			int iScore2 = Integer.valueOf(Bytes.toString(score2));
			String winner = (iScore1 > iScore2)?useridA:((iScore1 < iScore2)?useridB:"X");
			out.println(Bytes.toString(score1) + "\t" + Bytes.toString(score2) + "\t" + winner);
		}
	}
	catch (Exception e)
	{
		out.println(e);
	}
    }

    public void destroy() {
        // do nothing.
    }
}
