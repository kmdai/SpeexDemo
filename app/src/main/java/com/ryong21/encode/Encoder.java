package com.ryong21.encode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Encoder implements Runnable {

	private Logger log = LoggerFactory.getLogger(Encoder.class);
	private volatile int leftSize = 0;
	private final Object mutex = new Object();
	private Speex speex = new Speex();
	private int frameSize;
	private long ts;
	private byte[] processedData = new byte[1024];
	private short[] rawdata = new short[1024];
	private volatile boolean isRecording;

	public Encoder() {
		super();
		speex.init();
		frameSize = speex.getFrameSize();
		log.debug("frameSize {}", frameSize);

	}

	public void run() {

		android.os.Process
				.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

		int getSize = 0;
		while (this.isRecording()) {

			synchronized (mutex) {
				while (isIdle()) {
					try {
						mutex.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			synchronized (mutex) {
				getSize = speex.encode(rawdata, 0, processedData, leftSize);
				//只打印编码结果，没有后续处理
				log.debug("{} encoded size {}", ts, getSize);
				setIdle();
			}
		}
	}

	public void putData(long ts, short[] data, int size) {
		synchronized (mutex) {
			this.ts = ts;
			System.arraycopy(data, 0, rawdata, 0, size);
			this.leftSize = size;
			mutex.notify();
		}
	}

	public boolean isIdle() {
		return leftSize == 0 ? true : false;
	}

	public void setIdle() {
		leftSize = 0;
	}

	public void setRecording(boolean isRecording) {
		synchronized (mutex) {
			this.isRecording = isRecording;
			if (this.isRecording) {
				mutex.notify();
			}
		}
	}

	public boolean isRecording() {
		synchronized (mutex) {
			return isRecording;
		}
	}
}