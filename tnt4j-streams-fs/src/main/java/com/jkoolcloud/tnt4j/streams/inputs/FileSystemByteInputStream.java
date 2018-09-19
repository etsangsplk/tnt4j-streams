/*
 * Copyright 2014-2018 JKOOL, LLC.
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

import java.nio.file.FileSystem;

import com.jkoolcloud.tnt4j.sink.DefaultEventSinkFactory;
import com.jkoolcloud.tnt4j.sink.EventSink;
import com.jkoolcloud.tnt4j.streams.configure.StreamProperties;

/**
 * Implements a byte arrays based activity stream, where raw input source is {@link java.nio.file.FileSystem} provided
 * file {@link java.io.InputStream}. This class wraps the raw {@link java.io.InputStream} with a
 * {@link com.jkoolcloud.tnt4j.streams.inputs.feeds.StreamFeed}.
 * <p>
 * This activity stream requires parsers that can support {@link java.io.InputStream}s as the source for activity data.
 * <p>
 * List of supported file systems can be found in {@link com.jkoolcloud.tnt4j.streams.inputs.FileSystemAdapter}
 * documentation.
 * <p>
 * This activity stream supports configuration properties from {@link BytesInputStream} (and higher hierarchy streams)
 * in combination with properties from {@link com.jkoolcloud.tnt4j.streams.inputs.FileSystemAdapter}. It allows use of
 * {@code "FileName"} property in common with with {@code "Port"} property when accessing remote files.
 *
 * @version $Revision: 1 $
 *
 * @see FileSystemAdapter
 * @see com.jkoolcloud.tnt4j.streams.parsers.ActivityParser#isDataClassSupported(Object)
 */
public class FileSystemByteInputStream extends BytesInputStream {
	private static final EventSink LOGGER = DefaultEventSinkFactory.defaultEventSink(FileSystemByteInputStream.class);

	private FileSystemAdapter fsAdapter;

	/**
	 * Constructs an empty FileSystemByteInputStream. Requires configuration settings to set input stream source.
	 */
	public FileSystemByteInputStream() {
		super();

		fsAdapter = new FileSystemAdapter();
	}

	@Override
	protected EventSink logger() {
		return LOGGER;
	}

	@Override
	public void setProperty(String name, String value) {
		fsAdapter.setProperty(name, value);

		if (!StreamProperties.PROP_PORT.equalsIgnoreCase(name)) {
			super.setProperty(name, value);
		}
	}

	@Override
	public Object getProperty(String name) {
		Object pValue = fsAdapter.getProperty(name);
		return pValue == null ? super.getProperty(name) : pValue;
	}

	@Override
	protected void applyProperties() throws Exception {
		super.applyProperties();

		fileName = fsAdapter.initialize(fileName);
	}

	@Override
	protected FileSystem getFileSystem() throws Exception {
		return fsAdapter.getFileSystem();
	}
}
