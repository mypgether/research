package com.gether.research.kafka.streams.processor;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

/**
 * Created by myp on 2017/8/18.
 */
public class WordCountProcessor implements Processor<String,String> {

    private ProcessorContext context;
    private KeyValueStore<String, Long> kvStore;

    @Override
    public void init(ProcessorContext processorContext) {
        System.out.println("WordCountProcessor init");
        this.context = processorContext;
        this.context.schedule(1000);
        this.kvStore = (KeyValueStore) context.getStateStore("Counts");
    }

    @Override
    public void process(String dummy, String line) {
        System.out.println("WordCountProcessor process: " + dummy + "_____" + line);

        String[] words = line.toLowerCase().split(" ");

        for (String word : words) {
            Long oldValue = this.kvStore.get(word);

            if (oldValue == null) {
                this.kvStore.put(word, 1L);
            } else {
                this.kvStore.put(word, oldValue + 1L);
            }
        }
    }

    @Override
    public void punctuate(long timestamp) {
        System.out.println("WordCountProcessor punctuate: timestamp :" + timestamp);
        KeyValueIterator<String, Long> iter = this.kvStore.all();
        while (iter.hasNext()) {
            KeyValue<String, Long> entry = iter.next();
            context.forward(null, entry.key + entry.value.toString());
        }
        iter.close();
        // commit the current processing progress
        context.commit();
    }

    @Override
    public void close() {

    }
}