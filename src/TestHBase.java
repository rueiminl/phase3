import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class TestHBase {
	public static void main(String[] args) throws IOException
	{
		boolean testPut = false;
		boolean testGet = true;
		boolean testScan = false;
		Configuration config = HBaseConfiguration.create();
		HTable table = new HTable(config, "q3");
		if (testPut)
		{
			Put p = new Put(Bytes.toBytes(args[1]));
			p.add(Bytes.toBytes("v"), Bytes.toBytes(""), Bytes.toBytes("Some Value"));
			table.put(p);
		}

		if (testGet)
		{
			Get g = new Get(Bytes.toBytes("308118954"));
			Result r = table.get(g);
			byte [] value = r.getValue(Bytes.toBytes("v"),
				Bytes.toBytes(""));
			String valueStr = Bytes.toString(value);
			System.out.println(valueStr);
		}	
	
		if (testScan)
		{
			Scan s = new Scan();
			s.addColumn(Bytes.toBytes("myLittleFamily"), Bytes.toBytes("someQualifier"));
			ResultScanner scanner = table.getScanner(s);
			try {
				for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
				}
				for (Result rr : scanner) {
				}
			} finally {
				scanner.close();
			}	
		}
	}
}
