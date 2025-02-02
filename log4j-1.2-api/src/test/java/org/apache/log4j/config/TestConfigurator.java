/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.log4j.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.xml.XmlConfigurationFactory;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

public class TestConfigurator {

    static LoggerContext configure(final String configLocation) throws IOException {
        final File file = new File(configLocation);
        try (final InputStream inputStream = Files.newInputStream(Paths.get(configLocation))) {
            final ConfigurationSource source = new ConfigurationSource(inputStream, file);
            final LoggerContext context = (LoggerContext) org.apache.logging.log4j.LogManager.getContext(false);
            Configuration configuration = null;
            if (configLocation.endsWith(PropertiesConfigurationFactory.FILE_EXTENSION)) {
                configuration = new PropertiesConfigurationFactory().getConfiguration(context, source);
            } else if (configLocation.endsWith(XmlConfigurationFactory.FILE_EXTENSION)) {
                configuration = new XmlConfigurationFactory().getConfiguration(context, source);
            } else {
                fail("Test infra does not support " + configLocation);
            }
            assertNotNull("No configuration created", configuration);
            Configurator.reconfigure(configuration);
            return context;
        }
    }

}
