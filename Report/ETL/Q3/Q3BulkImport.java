import java.io.IOException;  
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.Map;  
import java.util.Map.Entry;  
import org.apache.hadoop.conf.Configuration;  
import org.apache.hadoop.fs.Path;	
import org.apache.hadoop.hbase.HBaseConfiguration;  
import org.apache.hadoop.hbase.client.HTable;  
import org.apache.hadoop.hbase.client.Put;  
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;  
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat;  
import org.apache.hadoop.hbase.util.Bytes;  
import org.apache.hadoop.io.LongWritable;  
import org.apache.hadoop.io.Text;	
import org.apache.hadoop.mapreduce.Job;  
import org.apache.hadoop.mapreduce.Mapper;  
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;  
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;  
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;	
public class Q3BulkImport {
	public static class Map extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {	    
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {  
			String line = value.toString();
			String[] strs = line.split("\t");
			//uid = strs[0]
			//rids = strs[1].replace(",", "\n");
			ImmutableBytesWritable HKey = new ImmutableBytesWritable(Bytes.toBytes(strs[0]));  
			Put HPut = new Put(Bytes.toBytes(strs[0]));
			HPut.add(Bytes.toBytes("v"), Bytes.toBytes(""), Bytes.toBytes(strs[1].replace(",", "\n")));  
			context.write(HKey, HPut);  
		}   
	}  
	public static void main(String[] args) throws Exception {  
		Configuration conf = HBaseConfiguration.create();  
		String inputPath = "s3://raylin/output.txt";  
		String outputPath = "/mnt/q3";	
		HTable hTable = new HTable(conf, "q3");  
		Job job = new Job(conf, "q3bulkimport");		  
		job.setMapOutputKeyClass(ImmutableBytesWritable.class);  
		job.setMapOutputValueClass(Put.class);	
		job.setSpeculativeExecution(false);  
		job.setReduceSpeculativeExecution(false);  
		job.setInputFormatClass(TextInputFormat.class);  
		job.setOutputFormatClass(HFileOutputFormat.class);  
		job.setJarByClass(Q3BulkImport.class);  
		job.setMapperClass(Map.class);  
		FileInputFormat.setInputPaths(job, inputPath);  
		FileOutputFormat.setOutputPath(job,new Path(outputPath));			
		HFileOutputFormat.configureIncrementalLoad(job, hTable);  
		System.exit(job.waitForCompletion(true) ? 0 : 1);  
	}	
}  
