import java.io.IOException;

 
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
 
public class NG {
	public static class Map1 extends Mapper<LongWritable, Text, Text, IntWritable> {
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

			Configuration conf = context.getConfiguration();
			int n = Integer.parseInt(conf.get("grams"));
			String words[];
			words = value.toString().split("");			
			for(int i = 0; i <= words.length - n; i++){
				StringBuilder s = new StringBuilder();
				word.set(s.toString());
				context.write(word, one);
			}
		}
	}
 
	public static class Reduce1 extends Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();
		public void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}
 
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("grams", args[2]);
		Job job = Job.getInstance(conf, "word count");
		job.setJarByClass(NG.class);	
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setOutputValueClass(IntWritable.class);
		job.setOutputKeyClass(Text.class);
		
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setMapperClass(Map1.class);
		job.setCombinerClass(Reduce1.class);
		job.setReducerClass(Reduce1.class);
		System.out.println("Done.");
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}