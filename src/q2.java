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
import org.apache.hadoop.hbase.KeyValue;
// Extend HttpServlet class
public class q2 extends HttpServlet {
    final String master = "172.31.42.82";
    final boolean useLocal = true;
    Configuration config;
    HTable table;
    public void init() throws ServletException 
    {
        try
        {
                config = HBaseConfiguration.create();
		if (!useLocal)
		{
			config.clear();
        	        config.set("hbase.zookeeper.quorum", master);
                	config.set("hbase.zookeeper.property.clientPort","2181");
	                config.set("hbase.master", master + ":60000");
		}
                table = new HTable(config, "q2");
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
		
        	String row = request.getParameter("userid") + "_" + request.getParameter("tweet_time");
	        Get g = new Get(Bytes.toBytes(row));
		g.setMaxVersions();
		Result r = table.get(g);
		KeyValue[] values = r.raw();
		for (KeyValue value : values)
		{
			out.println(Bytes.toString(value.getValue()));
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
