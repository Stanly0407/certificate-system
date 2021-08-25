//package com.epam.esm.connection;
//
//import org.apache.commons.dbcp2.BasicDataSource;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Properties;
//
//@Component
//public class ConnectionCreator {
//    private static final Logger LOGGER = LogManager.getLogger(ConnectionCreator.class);
//    private static final String PROPERTIES_FILENAME = "database.properties";
//    private static final String PROPERTY_URL = "CONNECTION_URL";
//    private static final String PROPERTIES_USER = "USER";
//    private static final String PROPERTIES_PASSWORD = "PASSWORD";
//    private static BasicDataSource basicDataSource;
//
//    @Autowired
//    private ConnectionCreator() {
//    }
//
//    static {
//        try (InputStream input = ConnectionCreator.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME)) {
//            Properties properties = new Properties();
//            properties.load(input);
//            basicDataSource.setUrl(properties.getProperty(PROPERTY_URL));
//            basicDataSource.setUsername(properties.getProperty(PROPERTIES_USER));
//            basicDataSource.setPassword(properties.getProperty(PROPERTIES_PASSWORD));
//            basicDataSource.setMinIdle(5);
//            basicDataSource.setMaxIdle(10);
//            basicDataSource.setMaxOpenPreparedStatements(100);
//        } catch (IOException e) {
//            LOGGER.error("ERROR (read properties): " + e + " | MESSAGE: " + e.getMessage());
//        }
//    }
//
//    public BasicDataSource getConnection() {
//        return basicDataSource;
//    }
//
//}

