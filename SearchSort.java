import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class SearchSort {

    public static class SearchMapper
            extends Mapper<LongWritable, Text, LongWritable, Text> { // first two for input, last two for output
        private Text str = new Text();

        public void map(LongWritable key, Text value, Context context
                        // first two for input key-value, Context is fromwork to emit key-value pairs
        ) throws IOException, InterruptedException {

            StringTokenizer itr = new StringTokenizer(value.toString());

            String str1 ="",  str2 ="";
            if (itr.hasMoreTokens())
                str1 = itr.nextToken();
            if (itr.hasMoreTokens())
                str2 = itr.nextToken();
            key.set(Long.parseLong(str2));
            str.set(str1);
            context.write(key, str);
        }
    }

    public static class SumReducer
            extends Reducer<LongWritable, Text, LongWritable, Text> {
        //private IntWritable result = new IntWritable();

        // Iterable<IntWritable> shuffle and sort key-value pairs,
        // values from the same key will put into a list, like word[1,1,1]

        public void reduce(Iterable<LongWritable> keys, Text value, Context context)
                throws IOException, InterruptedException {
            for (LongWritable val : keys) {
                context.write(val, value);
            }

        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");

        // Specify the jar file containes driver, mapper, reducer
        job.setJarByClass(SearchSort.class);

        // set mapper
        job.setMapperClass(SearchMapper.class);

        // set reducer
        job.setCombinerClass(SumReducer.class);
        job.setReducerClass(SumReducer.class);

        //Text.class, hadoop text class for general class
        job.setOutputKeyClass(LongWritable.class);

        // IntWritable, hadoop data types for int
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // set flag to indicate if the job complete
        boolean success = job.waitForCompletion(true);
        System.exit(success ? 0 : 1);
    }
}