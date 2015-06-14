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
package de.bitbrain.jpersis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Manages features for jpersis internally. This class is marked as package private for internal usage only.
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
class Features {

  static final String FILE = "features";
  private static final String DEV = "-developer";
  private static final String EXT = ".properties";

  private Properties properties;

  Features() {
    properties = new Properties();
    try {
      loadProperties(FILE + EXT);
      loadProperties(FILE + DEV + EXT);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  boolean isEnabled(Feature feature) {
    String val = properties.getProperty(feature.name().toLowerCase(), "false");
    return val.equals("true");
  }

  private void loadProperties(String path) throws IOException {
    Properties tmp = new Properties();
    ClassLoader loader = Features.class.getClassLoader();
    InputStream stream = loader.getResourceAsStream(path);
    try {
      if (stream != null) {
        tmp.load(stream);

        for (Entry<Object, Object> entry : tmp.entrySet()) {
          properties.setProperty((String) entry.getKey(), (String) entry.getValue());
        }
      } else {
        return;
      }
    } finally {
      if (stream != null) {
        stream.close();
      }
    }
  }

  enum Feature {
    MYSQL,
    POSTGRESQL;
  }
}
