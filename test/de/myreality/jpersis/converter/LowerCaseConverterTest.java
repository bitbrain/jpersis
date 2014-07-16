/*
 * Copyright 2014 Miguel Gonzalez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.myreality.jpersis.converter;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@see LowerCaseConverter}
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class LowerCaseConverterTest {
	
	LowerCaseConverter converter;
	
	@Before
	public void before() {
		converter = new LowerCaseConverter();
	}

	@Test
	public void testToJavaFormat() {
		assertTrue("The java format is wrong", converter.toJavaFormat("my_attribute_string").equals("MyAttributeString"));
	}
	
	@Test
	public void testToDatabaseFormat() {
		assertTrue("The database format is wrong", converter.toDatabaseFormat("MyAttributeString").equals("my_attribute_string"));
	}

}
