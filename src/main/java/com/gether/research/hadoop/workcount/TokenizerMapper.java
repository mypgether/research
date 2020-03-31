package com.gether.research.hadoop.workcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @author: fatiao
 * @date: 2019/12/11
 */
public class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

    IntWritable one = new IntWritable(1);
    Text word = new Text();

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        if (key != null) {
            System.out.println("key is " + key);
        } else {
            System.out.println("key is null");
        }

        StringTokenizer tokenizer = new StringTokenizer(value.toString());
        while (tokenizer.hasMoreTokens()) {
            word.set(tokenizer.nextToken());
            context.write(word, one);
        }
    }
}