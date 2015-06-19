package com.concurrentperformance.scratchpad.lmax;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * TODO Comments
 *
 * @author Lake
 */
public class LMAXThroughput {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public static final int RING_SIZE = 1 << 10;
	private final Executor EXECUTOR = Executors.newFixedThreadPool(2, new ThreadFactoryBuilder().setNameFormat("ProcessEvents_%d").build());

	private ProducerTranslator translator = new ProducerTranslator();

	private final Disruptor<Bucket> disruptor =
			new Disruptor<>(Bucket.BUCKET_FACTORY,
					RING_SIZE,
					EXECUTOR,
					ProducerType.SINGLE,
					new BlockingWaitStrategy());

	public LMAXThroughput(EventHandler<Bucket> handler) {
		disruptor.handleEventsWith(handler);
		disruptor.start();
	}

	public void putOntoBuffer(long id) {
		translator.setId(id);
		disruptor.publishEvent(translator);
	}

	class ProducerTranslator implements EventTranslator<Bucket> {

		private long id;

		public void setId(long id) {
			this.id = id;
		}

		@Override
		public void translateTo(Bucket bucket, long sequence) {
			bucket.setId(id);
		}

	}
}
