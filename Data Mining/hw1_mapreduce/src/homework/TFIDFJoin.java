package homework;

import homework.types.TermDocumentPair;
import homework.types.TypedRecord;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.DelegatingInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import java.util.*;
import java.io.*;

public class TFIDFJoin extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new TFIDFJoin(), args);
        System.exit(exitCode);
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Usage: <tf score path> <idf score path> <output path>");
            return -1;
        }

        // create a MapReduce job (put your student id below!)
        Job job = Job.getInstance(getConf(), "TFIDFJoin (2012-11249");

        // input & mapper
        job.setInputFormatClass(DelegatingInputFormat.class);
        MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, TFMapper.class);
        MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, IDFMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(TypedRecord.class);

        // reducer
        job.setReducerClass(TFIDFReducer.class);
        job.setOutputKeyClass(TermDocumentPair.class);
        job.setOutputValueClass(DoubleWritable.class);

        // output
        job.setOutputFormatClass(TextOutputFormat.class);
        FileOutputFormat.setOutputPath(job, new Path(args[2]));

        return job.waitForCompletion(true) ? 0 : -1;
    }
		
    public static class TFMapper extends Mapper<LongWritable, Text, Text, TypedRecord> {
        // fill your code here!
				private Text keyOut = new Text();
				private TypedRecord tr = new TypedRecord();

				@Override //////////input : result of TF(term, docid, tfscore), output : (term, TypedRecord(TF, tfscore))
				protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
						String line = value.toString();
						String[] words = line.split("\t");
						
						keyOut.set(words[0]);
						tr.setTFScore(Integer.parseInt(words[1]), Double.parseDouble(words[2]));
						
						context.write(keyOut, tr);
				}	
    }

    public static class IDFMapper extends Mapper<LongWritable, Text, Text, TypedRecord> {
        // fill your code here!
				private Text keyOut = new Text();
				private TypedRecord tr = new TypedRecord();

				@Override //////////input : result of IDF(term, idfscore), output : (term, TypedRecord(IDF, idfscore))
				protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
						String line = value.toString();
						String[] words = line.split("\t");
						
						keyOut.set(words[0]);
						tr.setIDFScore(Double.parseDouble(words[1]));

						context.write(keyOut, tr);
			}
    }
		
    public static class TFIDFReducer extends Reducer<Text, TypedRecord, TermDocumentPair, DoubleWritable> {
        // fill your code here!
				private TermDocumentPair pairOut = new TermDocumentPair();
				private DoubleWritable TFIDFScore = new DoubleWritable();
				
				@Override /////////input : result of TF, IDF(term, TypedRecord), output : (term, docId, tfidfscore)
				protected void reduce(Text key, Iterable<TypedRecord> values, Context context) throws IOException, InterruptedException {
						double temp = 0;
						double idfscore = 0;
						double tfscore = 0;
						ArrayList<String> tfidfs = new ArrayList<String>();
						for(TypedRecord tr : values) {
								if(tr.getType() == TypedRecord.RecordType.TF) {
										tfscore = tr.getScore();
										String s1 = Double.toString(tr.getScore());
										String s2 = Integer.toString(tr.getDocumentId());
										tfidfs.add(s1 + "/" + s2);
								}

								else if(tr.getType() == TypedRecord.RecordType.IDF) {
										idfscore = tr.getScore();
								}
						}
						///////sort tfidf score by descending order to show only top 10
						tfidfs.sort(new Comparator<String>()
								 {
								 		public int compare(String s1, String s2) {
												String[] words1 = s1.split("/");
												String[] words2 = s2.split("/");
												double d1 = Double.parseDouble(words1[0]);
												double d2 = Double.parseDouble(words2[0]);

												if(d1 < d2) return 1;
												else if(d1 > d2) return -1;
												else return 0;

										}

								 }
								 );
						///// show result at most 10
						for(int i = 0 ; i < tfidfs.size() ; i++) {
								if(i < 10) {
										String[] words = tfidfs.get(i).split("/");
										pairOut.set(key.toString(), Integer.parseInt(words[1]));
										TFIDFScore.set(Double.parseDouble(words[0]) * idfscore);

										context.write(pairOut, TFIDFScore);
								}
								else {
										break;
								}


						}



				}
    }
}

