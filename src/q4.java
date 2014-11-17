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
// Extend HttpServlet class
public class q4 extends HttpServlet {
    Configuration config;
    HTable table;
    public void init() throws ServletException 
    {
        try
        {
                config = HBaseConfiguration.create();
                table = new HTable(config, "q4");
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
                Get g = new Get(Bytes.toBytes(request.getParameter("location") + "____" + request.getParameter("date")));
		int m = Integer.valueOf(request.getParameter("m"));
		int n = Integer.valueOf(request.getParameter("n"));
		for (int rank = m; rank <= n; rank++)
		{
			Integer rk = rank;
	                g.addColumn(Bytes.toBytes("v"), Bytes.toBytes(rk.toString()));
		}
                Result r = table.get(g);
		for (int rank = m; rank <= n; rank++)
		{
			Integer rk = rank;
	                byte[] value = r.getValue(Bytes.toBytes("v"), Bytes.toBytes(rk.toString()));
			if (value == null)
				break;
			String valueStr = Bytes.toString(value);
			out.println(valueStr);
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
