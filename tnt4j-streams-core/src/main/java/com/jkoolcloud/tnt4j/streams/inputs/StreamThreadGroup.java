/*
 * Copyright 2014-2019 JKOOL, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jkoolcloud.tnt4j.streams.inputs;

import java.util.concurrent.TimeUnit;

import com.jkoolcloud.tnt4j.core.OpLevel;
import com.jkoolcloud.tnt4j.sink.EventSink;
import com.jkoolcloud.tnt4j.streams.utils.Duration;
import com.jkoolcloud.tnt4j.streams.utils.LoggerUtils;
import com.jkoolcloud.tnt4j.streams.utils.StreamsResources;

/**
 * Base class for threads running an TNTInputStream.
 *
 * @version $Revision: 1 $
 *
 * @see TNTInputStream
 */
public class StreamThreadGroup extends ThreadGroup {
	private static final EventSink LOGGER = LoggerUtils.getLoggerSink(StreamThreadGroup.class);

	/**
	 * Constructs a new streams runner threads group.
	 *
	 * @param name
	 *            the name of the new thread group
	 */
	public StreamThreadGroup(String name) {
		super(name);
	}

	/**
	 * Constructs a new streams runner threads group.
	 *
	 * @param parent
	 *            the parent thread group
	 * @param name
	 *            the name of the new thread group
	 */
	public StreamThreadGroup(ThreadGroup parent, String name) {
		super(parent, name);
	}

	/**
	 * Stops all pending threads of this group. Some APIs may leave some unterminated thread (i.e. schedulers) and thus
	 * prevent streams from completing in timely manner.
	 */
	void stopPendingJunkThreads() {
		Thread[] atl = new Thread[activeCount()];
		enumerate(atl, false);

		Duration tsd;
		for (Thread t : atl) {
			if (!(t instanceof StreamThread)) {
				LOGGER.log(OpLevel.WARNING, StreamsResources.getBundle(StreamsResources.RESOURCE_BUNDLE_NAME),
						"StreamThreadGroup.stopping.thread", t);
				tsd = Duration.arm();
				try {
					t.interrupt();
					t.join(TimeUnit.SECONDS.toMillis(5));
				} catch (Exception e) {
				}
				LOGGER.log(OpLevel.WARNING, StreamsResources.getBundle(StreamsResources.RESOURCE_BUNDLE_NAME),
						"StreamThreadGroup.stopped.thread", t, t.isAlive(), tsd.duration());

				if (t.isAlive()) {
					LOGGER.log(OpLevel.WARNING, StreamsResources.getBundle(StreamsResources.RESOURCE_BUNDLE_NAME),
							"StreamThreadGroup.killing.thread", t);
					tsd = Duration.arm();
					try {
						t.stop();
					} catch (ThreadDeath e) {
					}
					LOGGER.log(OpLevel.WARNING, StreamsResources.getBundle(StreamsResources.RESOURCE_BUNDLE_NAME),
							"StreamThreadGroup.killed.thread", t, tsd.duration());
				}
			}
		}
	}
}