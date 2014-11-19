import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.text.BreakIterator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
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
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Q2BulkImport {
	public static class Map extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {
	//public static class Map extends Mapper<LongWritable, Text, Text, Text> {
		JSONParser parser;
		SimpleDateFormat outDateFormat;
		SimpleDateFormat inDateFormat;
		HashMap<String, Integer> sentimentWords;
		HashSet<String> censoredWords;

		public void setup(Context context) throws IOException
		{
			inDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
			outDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			outDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

			parser = new JSONParser();

			Path[] localFiles = DistributedCache.getLocalCacheFiles(context.getConfiguration());
			if (localFiles.length < 1)
				return;

			sentimentWords = new HashMap<String, Integer>();
			BufferedReader r1 = new BufferedReader(new FileReader(localFiles[0].toString()));
			String line;
			while ((line = r1.readLine()) != null) {
				String[] sentimentWord = line.split("\t", 2);
				//sentimentWord[0]: word
				//sentimentWord[1]: score
				sentimentWords.put(sentimentWord[0], Integer.parseInt(sentimentWord[1]));
			}
			r1.close();

			if (localFiles.length < 2)
				return;
			censoredWords = new HashSet<String>();
			BufferedReader r2 = new BufferedReader(new FileReader(localFiles[1].toString()));
			while ((line = r2.readLine()) != null) {
				censoredWords.add(line);
			}
			r2.close();
		}
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			if (line.length() == 0)
				return;
			try
			{
				JSONObject tweetObject = (JSONObject)parser.parse(line);
				JSONObject user = (JSONObject)tweetObject.get("user");
				String userid = user.get("id").toString();
				String timestamp = outDateFormat.format(inDateFormat.parse((String)tweetObject.get("created_at")));
				String tweetid = tweetObject.get("id").toString();
				String tweetmsg = tweetObject.get("text").toString();
						
				int sentiments = 0;
				StringBuilder result = new StringBuilder(tweetmsg);
				BreakIterator it = BreakIterator.getWordInstance();
				it.setText(tweetmsg);
				int start = it.first();
				int end = it.next();
				while (end != BreakIterator.DONE){
					String word = tweetmsg.substring(start, end).toLowerCase();
					if (sentimentWords.containsKey(word)) {
						sentiments += sentimentWords.get(word);
					}
					if (censoredWords.contains(word)) {
						for (int i = start + 1; i < end - 1; i++){
							result.setCharAt(i, '*');
						}
					}
					start = end;
					end = it.next();
				}
				String keyStr = userid + "_" + timestamp;
				String valueStr = tweetid + ":" + sentiments + ":" + result;
				ImmutableBytesWritable HKey = new ImmutableBytesWritable(Bytes.toBytes(keyStr));
				Put HPut = new Put(Bytes.toBytes(keyStr));
				HPut.add(Bytes.toBytes("v"), Bytes.toBytes(""), Bytes.toBytes(valueStr));
				context.write(HKey, HPut);
			}
			catch (Exception e)
			{
				return;
			}
		}
	}

	// ref http://hadoopi.wordpress.com/2014/06/05/hadoop-add-third-party-libraries-to-mapreduce-job/
	private static void addJarToDistributedCache(Class classToAdd, Configuration conf) throws IOException {
		String jar = classToAdd.getProtectionDomain().getCodeSource().getLocation().getPath();
		File jarFile = new File(jar);
		Path hdfsJar = new Path("/mnt/lib/" + jarFile.getName());
		FileSystem hdfs = FileSystem.get(conf);
		hdfs.copyFromLocalFile(false, true, new Path(jar), hdfsJar);
		DistributedCache.addFileToClassPath(hdfsJar, conf);
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = HBaseConfiguration.create();
		DistributedCache.addCacheFile(new URI("/mnt/cache/AFINN.txt"), conf);
		DistributedCache.addCacheFile(new URI("/mnt/cache/banned.txt"), conf);
		addJarToDistributedCache(JSONParser.class, conf);

		String inputPath = "s3://15619f14twittertest2/600GB";
		//String inputPath = "/mnt/q2input";
		String outputPath = "/mnt/q2";
		HTable hTable = new HTable(conf, "q2");
		
		Job job = Job.getInstance(conf);
		job.setJarByClass(Q2BulkImport.class);
		job.setJobName("q2bulkimport");
		job.setMapOutputKeyClass(ImmutableBytesWritable.class);
		job.setMapOutputValueClass(Put.class);
		job.setSpeculativeExecution(false);
		job.setReduceSpeculativeExecution(false);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(HFileOutputFormat.class);
		job.setMapperClass(Map.class);  
		FileInputFormat.setInputPaths(job, inputPath);
		FileOutputFormat.setOutputPath(job, new Path(outputPath));
		HFileOutputFormat.configureIncrementalLoad(job, hTable);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}	
}
