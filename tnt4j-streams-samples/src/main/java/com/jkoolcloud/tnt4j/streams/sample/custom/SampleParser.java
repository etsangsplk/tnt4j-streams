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

package com.jkoolcloud.tnt4j.streams.sample.custom;

import java.text.ParseException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.jkoolcloud.tnt4j.core.OpLevel;
import com.jkoolcloud.tnt4j.sink.DefaultEventSinkFactory;
import com.jkoolcloud.tnt4j.sink.EventSink;
import com.jkoolcloud.tnt4j.streams.configure.ParserProperties;
import com.jkoolcloud.tnt4j.streams.fields.ActivityFieldLocator;
import com.jkoolcloud.tnt4j.streams.fields.ActivityInfo;
import com.jkoolcloud.tnt4j.streams.inputs.TNTInputStream;
import com.jkoolcloud.tnt4j.streams.parsers.GenericActivityParser;

/**
 * Sample custom parser.
 *
 * @version $Revision: 1 $
 */
public class SampleParser extends GenericActivityParser<String[]> {
	private static final EventSink LOGGER = DefaultEventSinkFactory.defaultEventSink(SampleParser.class);

	/**
	 * Defines field separator.
	 */
	protected String fieldDelim = DEFAULT_DELIM;

	/**
	 * Constructs an SampleParser.
	 */
	public SampleParser() {
		super();
	}

	@Override
	protected EventSink logger() {
		return LOGGER;
	}

	/**
	 * Sets custom properties for this parser
	 *
	 * @param props
	 *            properties to set
	 * @throws Exception
	 *             indicates error with properties
	 */
	@Override
	public void setProperties(Collection<Map.Entry<String, String>> props) throws Exception {
		if (props == null) {
			return;
		}
		for (Map.Entry<String, String> prop : props) {
			String name = prop.getKey();
			String value = prop.getValue();
			logger().log(OpLevel.DEBUG, "Setting {0} to ''{1}''", name, value);
			if (ParserProperties.PROP_FLD_DELIM.equalsIgnoreCase(name)) {
				fieldDelim = value;
			}
		}
	}

	@Override
	public ActivityInfo parse(TNTInputStream<?, ?> stream, Object data) throws IllegalStateException, ParseException {
		if (fieldDelim == null) {
			throw new IllegalStateException("SampleParser: field delimiter not specified or empty");
		}
		if (data == null) {
			return null;
		}
		// Get next string to parse
		String dataStr = getNextActivityString(data);
		if (StringUtils.isEmpty(dataStr)) {
			return null;
		}
		logger().log(OpLevel.DEBUG, "Parsing: {0}", dataStr);
		String[] fields = dataStr.split(fieldDelim);
		if (ArrayUtils.isEmpty(fields)) {
			logger().log(OpLevel.DEBUG, "Did not find any fields in input string");
			return null;
		}
		logger().log(OpLevel.DEBUG, "Split input into {0} fields", fields.length);

		return parsePreparedItem(stream, dataStr, fields);
	}

	/**
	 * Gets field value from raw data location and formats it according locator definition.
	 *
	 * @param locator
	 *            activity field locator
	 * @param fields
	 *            activity object data fields array
	 * @param formattingNeeded
	 *            flag to set if value formatting is not needed
	 * @return value formatted based on locator definition or {@code null} if locator is not defined
	 * @throws ParseException
	 *             if error applying locator format properties to specified value
	 * @see ActivityFieldLocator#formatValue(Object)
	 */
	@Override
	protected Object resolveLocatorValue(ActivityFieldLocator locator, String[] fields, AtomicBoolean formattingNeeded)
			throws ParseException {
		Object val = null;
		String locStr = locator.getLocator();
		int loc = Integer.parseInt(locStr);

		if (loc > 0 && loc <= fields.length) {
			val = fields[loc - 1].trim();
		}

		return val;
	}
}
