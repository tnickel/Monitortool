

package logging;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;


public class LoggerManager {
    private static final Logger logger = LogManager.getLogger(LoggerManager.class);

    public static void initializeLogger(String configPath) {
        File log4jConfigFile = new File(configPath);
        if (log4jConfigFile.exists()) {
            try {
                Configurator.initialize(null, log4jConfigFile.getAbsolutePath());
                logger.info("Log4j configuration file loaded successfully.");
            } catch (Exception e) {
                logger.error("Could not load Log4j configuration file, using default configuration.", e);
                configureDefaultLogger();
            }
        } else {
            logger.warn("Log4j configuration file not found, using default configuration.");
            configureDefaultLogger();
        }
    }

    private static void configureDefaultLogger() {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.setStatusLevel(org.apache.logging.log4j.Level.ERROR);
        builder.setConfigurationName("DefaultConfig");

        // Create Console Appender
        builder.add(builder.newAppender("Console", "CONSOLE")
                .add(builder.newLayout("PatternLayout")
                        .addAttribute("pattern", "%d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n")));

        // Create File Appender
        builder.add(builder.newAppender("LogToFile", "File")
                .addAttribute("fileName", "logs/application.log")
                .add(builder.newLayout("PatternLayout")
                        .addAttribute("pattern", "%d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n")));

        // Configure Root Logger
        builder.add(builder.newRootLogger(org.apache.logging.log4j.Level.INFO)
                .add(builder.newAppenderRef("Console"))
                .add(builder.newAppenderRef("LogToFile")));

        Configurator.initialize(builder.build());
    }
}