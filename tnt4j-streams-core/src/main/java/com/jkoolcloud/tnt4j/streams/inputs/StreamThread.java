/*
 * Copyright 2014-2016 JKOOL, LLC.
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

import com.jkoolcloud.tnt4j.streams.utils.StreamsThread;

/**
 * Base class for threads running an TNTInputStream.
 *
 * @version $Revision: 1 $
 *
 * @see TNTInputStream
 */
public class StreamThread extends StreamsThread {

	/**
	 * TNTInputStream being executed by this thread.
	 */
	protected final TNTInputStream<?, ?> target;

	/**
	 * Creates thread to run specified TNTInputStream.
	 *
	 * @param target
	 *            the TNTInputStream to run
	 * @see Thread#Thread(Runnable)
	 */
	public StreamThread(TNTInputStream<?, ?> target) {
		super(target);
		this.target = target;
		target.setOwnerThread(this);
	}

	/**
	 * Creates thread to run specified TNTInputStream.
	 *
	 * @param target
	 *            the TNTInputStream to run
	 * @param name
	 *            the name for thread
	 * @see Thread#Thread(Runnable, String)
	 */
	public StreamThread(TNTInputStream<?, ?> target, String name) {
		super(target, name);
		this.target = target;
		target.setOwnerThread(this);
	}

	/**
	 * Creates thread to run specified TNTInputStream.
	 *
	 * @param group
	 *            the thread group new thread is to belong to
	 * @param target
	 *            the TNTInputStream to run
	 * @param name
	 *            the name for thread
	 * @see Thread#Thread(ThreadGroup, Runnable, String)
	 */
	public StreamThread(ThreadGroup group, TNTInputStream<?, ?> target, String name) {
		super(group, target, name);
		this.target = target;
		target.setOwnerThread(this);
	}

	/**
	 * Creates thread to run specified TNTInputStream.
	 *
	 * @param group
	 *            the thread group new thread is to belong to
	 * @param target
	 *            the TNTInputStream to run
	 * @see Thread#Thread(ThreadGroup, Runnable)
	 */
	public StreamThread(ThreadGroup group, TNTInputStream<?, ?> target) {
		super(group, target);
		this.target = target;
		target.setOwnerThread(this);
	}

	/**
	 * Gets the TNTInputStream being run by this thread.
	 *
	 * @return TNTInputStream being run
	 */
	public TNTInputStream<?, ?> getTarget() {
		return target;
	}
}