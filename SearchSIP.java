import java.io.IOException;
import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class SearchSIP {
    public static class SearchMapper
            extends Mapper<Object, Text, Text, IntWritable> { // first two for input, last two for output

        private final static IntWritable one = new IntWritable(1);
        private String searchString = "10.153.239.5";

        @Override
        public void map(Object key, Text value, Context context
                        // first two for input key-value, Context is fromwork to emit key-value pairs
        ) throws IOException, InterruptedException {

/*            // get string from value (Text convert to java String first )
            // defautlt: split string to word by word by StringTokenizer
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                context.write(word, one);
            }*/

            String line = value.toString();
            if(line.contains(searchString))
                context.write(new Text(searchString) ,one);

        }
    }

    public static class SumReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        @Override
        // Iterable<IntWritable> shuffle and sort key-value pairs,
        // values from the same key will put into a list, like word[1,1,1]
        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {

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
        Job job = Job.getInstance(conf, "word count");

        // Specify the jar file containes driver, mapper, reducer
        job.setJarByClass(SearchSIP.class);

        // set mapper
        job.setMapperClass(SearchMapper.class);

        // set reducer
        job.setCombinerClass(SumReducer.class);
        job.setReducerClass(SumReducer.class);

        //Text.class, hadoop text class for general class
        job.setOutputKeyClass(Text.class);

        // IntWritable, hadoop data types for int
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // set flag to indicate if the job complete
        boolean success = job.waitForCompletion(true);
        System.exit(success ? 0 : 1);
    }
}

