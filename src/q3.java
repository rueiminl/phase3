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
    Configuration config;
    HTable table;
    public void init() throws ServletException
    {
	try
	{
		config = HBaseConfiguration.create();
		config.clear();
		config.set("hbase.zookeeper.quorum", "172.31.46.34");
		config.set("hbase.zookeeper.property.clientPort","2181");
		config.set("hbase.master", "172.31.46.34:60000");
		table = new HTable(config, "q3");
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
