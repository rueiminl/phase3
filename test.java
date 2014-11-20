
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
import org.apache.hadoop.hbase.client.HBaseAdmin;
// Extend HttpServlet class
public class test {
	public static void main(String[] args) throws Exception
	{
	    Configuration config;
	    HTable table;
	try
	{
		config = HBaseConfiguration.create();
config.clear();
		System.out.println("1");
config.set("hbase.zookeeper.quorum", "ec2-54-172-241-96.compute-1.amazonaws.com");
		System.out.println("1");
config.set("hbase.zookeeper.property.clientPort","2181");
		System.out.println("1");
//config.set("hbase.master", "ec2-54-172-241-96.compute-1.amazonaws.com:60000");
		System.out.println("12");
		HBaseAdmin.checkHBaseAvailable(config);
		table = new HTable(config, "q3");
		System.out.println("10");
                Get g = new Get(Bytes.toBytes("12"));
                Result r = table.get(g);
                byte [] value = r.getValue(Bytes.toBytes("v"),
                        Bytes.toBytes(""));
		String valueStr = Bytes.toString(value);
		System.out.println(valueStr);
		System.out.println("4");
	}
	catch (Exception e)
	{
		System.out.println(e);
		System.out.println("3");
	}

		System.out.println("2");
    }
}
