package com.concurrentperformance.scratchpad.lmax;

import com.lmax.disruptor.EventFactory;

public class Bucket {

    private long id;

    public final static EventFactory<Bucket> BUCKET_FACTORY = () -> new Bucket();

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
