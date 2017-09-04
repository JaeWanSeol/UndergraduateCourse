package homework;

import homework.types.TermDocumentPair;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.*;

public class TermFrequency extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new TermFrequency(), args);
        System.exit(exitCode);
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: <input path> <output path>");
            return -1;
        }

        // create a MapReduce job (put your student id below!)
        Job job = Job.getInstance(getConf(), "TermFrequency (2012-11249)");

        // input
        job.setInputFormatClass(TextInputFormat.class);
        FileInputFormat.setInputPaths(job, new Path(args[0]));

        // mapper
        job.setMapperClass(TFMapper.class);
        job.setMapOutputKeyClass(TermDocumentPair.class);
        job.setMapOutputValueClass(DoubleWritable.class);

        // reducer
        job.setNumReduceTasks(0); // we use only mapper for this MapReduce job!
        job.setOutputKeyClass(TermDocumentPair.class);
        job.setOutputValueClass(DoubleWritable.class);

        // output
        job.setOutputFormatClass(TextOutputFormat.class);
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        return job.waitForCompletion(true) ? 0 : -1;
    }

    public static class TFMapper extends Mapper<LongWritable, Text, TermDocumentPair, DoubleWritable> {
        // fill your code here!
				TermDocumentPair pairOut = new TermDocumentPair();
				
				@Override
				protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
						String line = value.toString();
						HashMap<String, Integer> frqs = new HashMap<String, Integer>();
						//split docId and words
						String[] words1 = line.split("\t");
						//split words by space
						String[] words2 = words1[1].split(" ");

						int maxFrq = -1;

						for(String word : words2) {
								//use HashMap to calculate TermFrequency
								if(frqs.get(word) == null) {
										frqs.put(word, 1);
								}
								else {
										int temp = frqs.get(word);
										frqs.put(word, temp+1);
								}
								//find maxFrq
								if(maxFrq < frqs.get(word)) {
									maxFrq = frqs.get(word);
								}
						}

						Set<String> keyset = frqs.keySet();

						for(String word : keyset) {
								//calculate TermFrequency
								double temp = 0.5 + 0.5 * (frqs.get(word)) / maxFrq;
								DoubleWritable tfScore = new DoubleWritable(temp);
								pairOut.set(word, Integer.parseInt(words1[0]));
								context.write(pairOut, tfScore);
						}
				}

    }
}

