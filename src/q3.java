package mypackage;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.math.*;
import java.util.*;
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
public class q3 extends HttpServlet {
    private DataSource dataSource;
    public void init() throws ServletException 
    {
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
		Configuration config = HBaseConfiguration.create();
		HTable table = new HTable(config, "q3");
                Get g = new Get(Bytes.toBytes(request.getParameter("userid")));
                Result r = table.get(g);
                byte [] value = r.getValue(Bytes.toBytes("v"),
                        Bytes.toBytes(""));
		String valueStr = Bytes.toString(value);
		out.println(valueStr);
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
