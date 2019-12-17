package com.gether.research.hadoop.workcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author: fatiao
 * @date: 2019/12/11
 */
public class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    IntWritable count = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        System.out.println("before reduce key: " + key.toString());

        int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }
        count.set(sum);
        System.out.println("after reduce key: " + key.toString() + " count: " + count.get());

        context.write(key, count);
    }
}