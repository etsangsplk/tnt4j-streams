/*
 * Copyright 2014-2016 JKOOL, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.jkool.tnt4j.streams.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * @author akausinis
 * @version 1.0
 */
public class TimestampFormatterTest {

	@Test
	public void testConvert() throws ParseException {
		Number testNum = 100000L;
		for (TimeUnit fromUnits : TimeUnit.values()) {
			for (TimeUnit toUnits : TimeUnit.values()) {
				final Number convert = TimestampFormatter.convert(testNum, fromUnits, toUnits);
				final Number convertBack = TimestampFormatter.convert(testNum, toUnits, fromUnits);
			}
		}
	}

	@Test
	public void testParse() throws ParseException {
		final Date date = new Date();
		TimestampFormatter formatter = new TimestampFormatter(TimeUnit.MILLISECONDS);
		assertNotNull(TimestampFormatter.parse(TimeUnit.MILLISECONDS, date));
		assertNotNull(formatter.parse(date));
		assertNotNull(TimestampFormatter.parse(TimeUnit.MICROSECONDS, Calendar.getInstance()));
		assertNotNull(formatter.parse(Calendar.getInstance()));
		assertNotNull(TimestampFormatter.parse(TimeUnit.SECONDS, date.getTime()));
		assertNotNull(formatter.parse(String.valueOf(date.getTime())));
		assertNotNull(formatter.parse(date.getTime()));
		// formatter.setPattern("# ", Locale.FRENCH.toString()); XXX
		// assertNotNull(formatter.parse(String.valueOf(date.getTime())));
	}

	@Test(expected = ParseException.class)
	public void testParseExcepion() throws ParseException {
		TimestampFormatter.parse(TimeUnit.MICROSECONDS, "TEST");
	}

	@Test(expected = ParseException.class)
	public void testParseExcepion2() throws ParseException {
		TimestampFormatter formatter = new TimestampFormatter(TimeUnit.MILLISECONDS);
		formatter.parse(this);
	}

	@Test
	public void testTimeZone() {
		TimestampFormatter formatter = new TimestampFormatter(TimeUnit.MILLISECONDS);
		final String timezone = TimeZone.getDefault().toString();
		formatter.setTimeZone(timezone);
		assertEquals(timezone, formatter.getTimeZone());
	}
}
