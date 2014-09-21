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
package de.bitbrain.jpersis.drivers;

/**
 * Provides access to a driver
 * 
 * @author Miguel Gonzalez
 * @since 1.0
 * @version 1.0
 */
public interface DriverProvider {

	/**
	 * Sets a new driver
	 * 
	 * @param driver new driver
	 */
	void setDriver(Driver driver);
	
	/**
	 * Returns the current driver
	 * 
	 * @return current driver
	 */
	Driver getDriver();
}
