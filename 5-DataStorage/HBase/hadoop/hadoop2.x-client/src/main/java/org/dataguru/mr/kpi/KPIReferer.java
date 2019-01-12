package org.dataguru.mr.kpi;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

public class KPIReferer {

    public static class KPIIPMapper extends MapReduceBase implements Mapper<Object, Text, Text, Text> {
        private Text word = new Text();
        private Text ips = new Text();

        public void map(Object key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
            KPI kpi = KPI.filterIPs(value.toString());
            if (kpi.isValid()) {
                word.set(kpi.getHttp_referer_domain());
                ips.set(kpi.getRemote_addr());
                output.collect(word, ips);
            }
        }
    }

    public static class KPIIPReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
        private Text result = new Text();
        private Set<String> count = new HashSet<String>();

        public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
            while (values.hasNext()) {
                count.add(values.next().toString());
            }
            result.set(String.valueOf(count.size()));
            output.collect(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
    	
    	System.setProperty("hadoop.home.dir", "E:/dataguru/soft/hadoop-common-2.2.0-bin-master"); 
    	
        String input = "hdfs://192.168.1.101:9000/in/access.20120104.log";
        String output = "hdfs://192.168.1.101:9000/out/referer";

        JobConf conf = new JobConf(KPIReferer.class);
        conf.setJobName("KPIReferer");
        conf.setMapOutputKeyClass(Text.class);
        conf.setMapOutputValueClass(Text.class);
        
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(Text.class);
        
        conf.setMapperClass(KPIIPMapper.class);
        conf.setCombinerClass(KPIIPReducer.class);
        conf.setReducerClass(KPIIPReducer.class);

        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);

        FileInputFormat.setInputPaths(conf, new Path(input));
        FileOutputFormat.setOutputPath(conf, new Path(output));

        JobClient.runJob(conf);
        System.exit(0);
    }

}