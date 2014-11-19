package mypackage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
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
public class q6 extends HttpServlet {
    TreeMap<Long, Integer> data;
    public void init() throws ServletException 
    {
        try
        {
		data = new TreeMap<Long, Integer>();
		data.put(0L, 0);
		BufferedReader r = new BufferedReader(new FileReader(getServletContext().getRealPath("q6.txt")));
		String line;
		while ((line = r.readLine()) != null)
		{
			String[] nums = line.split("\t");
			data.put(Long.parseLong(nums[0]), Integer.parseInt(nums[1]));
		}
        }
        catch (Exception e)
        {
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set response content type
        response.setContentType("text/plain");
        // Actual logic goes here.
        PrintWriter out = response.getWriter();
        out.println("Wolken,5534-0848-5100,0299-6830-9164");
	try 
	{
		long m = Long.parseLong(request.getParameter("m")) - 1;
		long n = Long.parseLong(request.getParameter("n"));
		Map.Entry<Long,Integer> e1 = data.floorEntry(m);
		Map.Entry<Long,Integer> e2 = data.floorEntry(n);
		out.println(e2.getValue() - e1.getValue());
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
