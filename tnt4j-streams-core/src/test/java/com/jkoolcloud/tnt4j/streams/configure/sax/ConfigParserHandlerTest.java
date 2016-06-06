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

package com.jkoolcloud.tnt4j.streams.configure.sax;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.AttributesImpl;

import com.jkoolcloud.tnt4j.streams.inputs.TNTInputStream;
import com.jkoolcloud.tnt4j.streams.inputs.TNTParseableInputStream;
import com.jkoolcloud.tnt4j.streams.utils.Utils;

/**
 * @author akausinis
 * @version 1.0
 */
public class ConfigParserHandlerTest {

	private static List<String> skipConfigurationsList;
	private static File samplesDir;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		initSamplesDir();
	}

	private static void initSamplesDir() throws Exception {
		skipConfigurationsList = new ArrayList<String>();

		samplesDir = new File("./samples/");
		if (!samplesDir.isDirectory()) {
			samplesDir = new File("./tnt4j-streams-core/samples/");
			if (!samplesDir.isDirectory()) {
				fail("Samples root directory doesn't exist");
			}
		} else {
			skipConfigurationsList.add("java-stream");
		}
	}

	@Test
	public void streamsSamplesConfigTest() throws Exception {
		validateConfigs(samplesDir, "tnt-data-source*.xml", true, skipConfigurationsList);
		validateConfigs(samplesDir, "parsers*.xml", false, null);
	}

	protected void validateConfigs(File samplesDir, String configFileWildcard, boolean checkStreams,
			List<String> skipFiles) throws Exception {
		Collection<File> sampleConfigurations = FileUtils.listFiles(samplesDir,
				FileFilterUtils
						.asFileFilter((FilenameFilter) new WildcardFileFilter(configFileWildcard, IOCase.INSENSITIVE)),
				TrueFileFilter.INSTANCE);

		Collection<File> sampleConfigurationsFiltered = new ArrayList<File>(sampleConfigurations);
		if (CollectionUtils.isNotEmpty(skipFiles)) {
			for (File sampleConfiguration : sampleConfigurations) {
				for (String skipFile : skipFiles) {
					if (sampleConfiguration.getAbsolutePath().contains(skipFile))
						sampleConfigurationsFiltered.remove(sampleConfiguration);
				}
			}
		}

		for (File sampleConfiguration : sampleConfigurationsFiltered) {
			System.out.println("Reading configuration file: " + sampleConfiguration.getAbsolutePath());
			Reader testReader = new FileReader(sampleConfiguration);
			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			SAXParser parser = parserFactory.newSAXParser();
			ConfigParserHandler hndlr = new ConfigParserHandler();
			parser.parse(new InputSource(testReader), hndlr);

			assertNotNull("Parsed streams config data is null", hndlr.getStreamsConfigData());
			boolean parseable = true;
			if (checkStreams) {
				assertTrue("No configured streams", hndlr.getStreamsConfigData().isStreamsAvailable());

				parseable = false;
				for (TNTInputStream<?, ?> s : hndlr.getStreamsConfigData().getStreams()) {
					if (s instanceof TNTParseableInputStream) {
						parseable = true;
						break;
					}
				}
			}
			if (parseable) {
				assertTrue("No configured parsers", hndlr.getStreamsConfigData().isParsersAvailable());
			}

			Utils.close(testReader);
		}
	}

	@Test
	public void startElementTest() throws Exception {
		ConfigParserHandler test = Mockito.mock(ConfigParserHandler.class, Mockito.CALLS_REAL_METHODS);
		AttributesImpl attrs = new AttributesImpl();
		test.startDocument();
		attrs.addAttribute("", "", "name", "", "Stream attr name");
		attrs.addAttribute("", "", "type", "", "java.lang.String");
		attrs.addAttribute("", "", "class", "", "Stream attr class");
		attrs.addAttribute("", "", "filter", "", "Stream attr filter");
		attrs.addAttribute("", "", "rule", "", "Stream attr rule");
		attrs.addAttribute("", "", "step", "", "Stream attr step");
		attrs.addAttribute("", "", "tnt4j-properties", "", "Stream attr tnt4j-properties");
		attrs.addAttribute("", "", "java-object", "", "Stream attr java-object");
		attrs.addAttribute("", "", "param", "", "Stream attr param");
		attrs.addAttribute("", "", "tags", "", "Stream attr tags");
		attrs.addAttribute("", "", "value", "", "Stream attr value");

		test.startElement("TEST_URL", "TEST_LOCALNAME", "filter", attrs);
		test.startElement("TEST_URL", "TEST_LOCALNAME", "rule", attrs);
		test.startElement("TEST_URL", "TEST_LOCALNAME", "step", attrs);
		test.startElement("TEST_URL", "TEST_LOCALNAME", "java-object", attrs);
		// test.startElement("TEST_URL", "TEST_LOCALNAME", "tnt4j-properties",
		// attrs);
		test.startElement("TEST_URL", "TEST_LOCALNAME", "param", attrs);
		test.startElement("TEST_URL", "TEST_LOCALNAME", "tnt-data-source", attrs);
	}

	@Test(expected = SAXParseException.class)
	public void processParserTest1() throws Exception {
		ConfigParserHandler test = Mockito.mock(ConfigParserHandler.class, Mockito.CALLS_REAL_METHODS);
		AttributesImpl attrs = new AttributesImpl();
		attrs.addAttribute("", "", "name", "", "Stream attr name");
		attrs.addAttribute("", "", "type", "", "Stream attr type");
		// attrs.addAttribute("", "", "class", "", "Stream attr class");
		attrs.addAttribute("", "", "filter", "", "Stream attr filter");
		attrs.addAttribute("", "", "rule", "", "Stream attr rule");
		attrs.addAttribute("", "", "step", "", "Stream attr step");
		attrs.addAttribute("", "", "tnt4j-properties", "", "Stream attr tnt4j-properties");
		attrs.addAttribute("", "", "java-object", "", "Stream attr java-object");
		attrs.addAttribute("", "", "param", "", "Stream attr param");
		attrs.addAttribute("", "", "tags", "", "Stream attr tags");
		test.startElement("TEST_URL", "TEST_LOCALNAME", "parser", attrs);
	}

	@Test(expected = SAXParseException.class)
	public void processParserTest2() throws Exception {
		ConfigParserHandler test = Mockito.mock(ConfigParserHandler.class, Mockito.CALLS_REAL_METHODS);
		AttributesImpl attrs = new AttributesImpl();
		// attrs.addAttribute("", "", "name", "", "Stream attr name");
		attrs.addAttribute("", "", "type", "", "Stream attr type");
		attrs.addAttribute("", "", "class", "", "Stream attr class");
		attrs.addAttribute("", "", "filter", "", "Stream attr filter");
		attrs.addAttribute("", "", "rule", "", "Stream attr rule");
		attrs.addAttribute("", "", "step", "", "Stream attr step");
		attrs.addAttribute("", "", "tnt4j-properties", "", "Stream attr tnt4j-properties");
		attrs.addAttribute("", "", "java-object", "", "Stream attr java-object");
		attrs.addAttribute("", "", "param", "", "Stream attr param");
		attrs.addAttribute("", "", "tags", "", "Stream attr tags");
		test.startElement("TEST_URL", "TEST_LOCALNAME", "parser", attrs);
	}

	@Test(expected = SAXException.class)
	public void processParserTryCatchTest() throws Exception {
		ConfigParserHandler test = Mockito.mock(ConfigParserHandler.class, Mockito.CALLS_REAL_METHODS);
		AttributesImpl attrs = new AttributesImpl();
		test.startDocument();
		attrs.addAttribute("", "", "name", "", "Stream attr name");
		attrs.addAttribute("", "", "type", "", "Stream attr type");
		attrs.addAttribute("", "", "class", "", "java.lang.String");
		attrs.addAttribute("", "", "filter", "", "Stream attr filter");
		attrs.addAttribute("", "", "rule", "", "Stream attr rule");
		attrs.addAttribute("", "", "step", "", "Stream attr step");
		attrs.addAttribute("", "", "tnt4j-properties", "", "Stream attr tnt4j-properties");
		attrs.addAttribute("", "", "java-object", "", "Stream attr java-object");
		attrs.addAttribute("", "", "param", "", "Stream attr param");
		attrs.addAttribute("", "", "tags", "", "Stream attr tags");
		test.startElement("TEST_URL", "TEST_LOCALNAME", "parser", attrs);
	}

	@Test
	public void processFieldTest() throws Exception {
		ConfigParserHandler test = Mockito.mock(ConfigParserHandler.class, Mockito.CALLS_REAL_METHODS);
		test.startDocument();
		AttributesImpl attrs = new AttributesImpl();
		attrs.addAttribute("", "", "name", "", "Stream attr name");
		attrs.addAttribute("", "", "radix", "", "555");
		attrs.addAttribute("", "", "required", "", "Requered");
		attrs.addAttribute("", "", "locator", "", "");
		attrs.addAttribute("", "", "value", "", "555");
		attrs.addAttribute("", "", "units", "", "Units");
		attrs.addAttribute("", "", "format", "", "YYYY-mm-dd HH:mm:ss");
		attrs.addAttribute("", "", "locale", "", "lt_LT");
		attrs.addAttribute("", "", "timezone", "", "Europe/Vilnius");
		attrs.addAttribute("", "", "class", "", "com.jkoolcloud.tnt4j.streams.parsers.ActivityTokenParser");
		test.startElement("TEST_URL", "TEST_LOCALNAME", "parser", attrs);
		test.startElement("TEST_URL", "TEST_LOCALNAME", "field", attrs);
	}

	@Test(expected = SAXParseException.class)
	public void processFieldExceptionTest() throws Exception {
		ConfigParserHandler test = Mockito.mock(ConfigParserHandler.class, Mockito.CALLS_REAL_METHODS);
		AttributesImpl attrs = new AttributesImpl();
		test.startElement("TEST_URL", "TEST_LOCALNAME", "field", attrs);
	}

	@Test(expected = SAXParseException.class)
	public void missingAttributeTest() throws Exception {
		ConfigParserHandler test = Mockito.mock(ConfigParserHandler.class, Mockito.CALLS_REAL_METHODS);
		AttributesImpl attrs = new AttributesImpl();
		test.startDocument();
		attrs.addAttribute("", "", "name", "", "Stream name value");
		attrs.addAttribute("", "", "value", "", "");
		attrs.addAttribute("", "", "class", "", "com.jkoolcloud.tnt4j.streams.parsers.ActivityTokenParser");
		test.startElement("TEST_URL", "TEST_LOCALNAME", "parser", attrs);
		attrs.addAttribute("", "", "name", "", "");
		test.startElement("TEST_URL", "TEST_LOCALNAME", "field", attrs);
	}

	@Test
	public void locatorSplitingEmptyElementTest() throws Exception {
		ConfigParserHandler test = Mockito.mock(ConfigParserHandler.class, Mockito.CALLS_REAL_METHODS);
		test.startDocument();
		AttributesImpl attrs = new AttributesImpl();
		attrs.addAttribute("", "", "name", "", "Stream attr name");
		attrs.addAttribute("", "", "locator", "", "|555");
		attrs.addAttribute("", "", "value", "", "");
		attrs.addAttribute("", "", "class", "", "com.jkoolcloud.tnt4j.streams.parsers.ActivityTokenParser");
		test.startElement("TEST_URL", "TEST_LOCALNAME", "parser", attrs);
		test.startElement("TEST_URL", "TEST_LOCALNAME", "field", attrs);
	}

	@Test(expected = SAXParseException.class)
	public void processFieldLocatorTest() throws Exception {
		ConfigParserHandler test = Mockito.mock(ConfigParserHandler.class, Mockito.CALLS_REAL_METHODS);
		test.startDocument();
		AttributesImpl attrs = new AttributesImpl();
		attrs.addAttribute("", "", "name", "", "Stream attr name");
		attrs.addAttribute("", "", "datatype", "", "Timestamp");
		attrs.addAttribute("", "", "radix", "", "555");
		attrs.addAttribute("", "", "required", "", "Requered");
		attrs.addAttribute("", "", "units", "", null);
		attrs.addAttribute("", "", "format", "", "YYYY-mm-dd HH:mm:ss");
		attrs.addAttribute("", "", "locale", "", "lt_LT");
		attrs.addAttribute("", "", "timezone", "", "Europe/Vilnius");
		attrs.addAttribute("", "", "class", "", "com.jkoolcloud.tnt4j.streams.parsers.ActivityTokenParser");
		test.startElement("TEST_URL", "TEST_LOCALNAME", "parser", attrs);
		test.startElement("TEST_URL", "TEST_LOCALNAME", "field", attrs);
		attrs.addAttribute("", "", "value", "", "");
		test.startElement("TEST_URL", "TEST_LOCALNAME", "field-locator", attrs);
	}

	@Test(expected = SAXParseException.class)
	public void processFieldLocatorExceptionTest() throws Exception {
		ConfigParserHandler test = Mockito.mock(ConfigParserHandler.class, Mockito.CALLS_REAL_METHODS);
		test.startDocument();
		AttributesImpl attrs = new AttributesImpl();
		attrs.addAttribute("", "", "name", "", "Stream attr name");
		attrs.addAttribute("", "", "class", "", "com.jkoolcloud.tnt4j.streams.parsers.ActivityTokenParser");
		attrs.addAttribute("", "", "datatype", "", "DateTime");
		attrs.addAttribute("", "", "format", "", null);
		test.startElement("TEST_URL", "TEST_LOCALNAME", "parser", attrs);
		test.startElement("TEST_URL", "TEST_LOCALNAME", "field", attrs);
		attrs.addAttribute("", "", "value", "", "");
		test.startElement("TEST_URL", "TEST_LOCALNAME", "field-locator", attrs);
	}
}
