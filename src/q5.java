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
                config.set("hbase.zookeeper.quorum", "172.31.46.34");
                config.set("hbase.zookeeper.property.clientPort","2181");
                config.set("hbase.master", "172.31.46.34:60000");
                table = new HTable(config, "q5");
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
	StringBuilder result = new StringBuilder(256);
	result.append("Wolken,5534-0848-5100,0299-6830-9164\n");
	try 
	{
        	String useridA = request.getParameter("m");
		String useridB = request.getParameter("n");
		result.append(useridA);
		result.append("\t");
		result.append(useridB);
		result.append("\tWINNER\n");
		List<Get> gs = new ArrayList<Get>();
	        gs.add(new Get(Bytes.toBytes(useridA)));
                gs.add(new Get(Bytes.toBytes(useridB)));
		Result[] results = table.get(gs);
		KeyValue[] values1 = results[0].raw();
		KeyValue[] values2 = results[1].raw();
		for (int i = 0; i < 4; i++)
		{
			String score1 = Bytes.toString(values1[i].getValue());
			String score2 = Bytes.toString(values2[i].getValue());
			int iScore1 = Integer.parseInt(score1);
			int iScore2 = Integer.parseInt(score2);
			String winner = (iScore1 > iScore2)?useridA:((iScore1 < iScore2)?useridB:"X");
			result.append(score1);
			result.append("\t");
			result.append(score2);
			result.append("\t");
			result.append(winner);
			result.append("\n");
		}
		out.print(result);
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
