package com.concurrentperformance.scratchpad.lmax;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author Steve Lake
 */
public class LMAXThroughputTest {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Test
	public void testThroughput() {

		final int iterations = 100000000;
		final CountDownLatch finished = new CountDownLatch(1);

		LMAXThroughput lmaxThroughput = new LMAXThroughput((bucket, sequence, endOfBatch) -> {
			if (sequence == bucket.getId()) {
				finished.countDown();
			}

			if (endOfBatch) {
				//log.info("[{}]", sequence);
			}
		});

		long start = System.currentTimeMillis();

		for (int i=0;i<iterations;i++) {
			lmaxThroughput.putOntoBuffer(1);
		}
		long sumbissionEnd = System.currentTimeMillis();
		long submissionDuration = sumbissionEnd - start;
		log.info("Finished Submission. Time [{}}ms", submissionDuration);


		try {
			finished.await();
		} catch (InterruptedException e) {
			log.error("TODO", e);
		}

		long end = System.currentTimeMillis();
		long duration = end - start;
		log.info(" Time [{}]ms. Throughput [{}] / second", duration, (iterations / duration) * 1000 );

	}
}
