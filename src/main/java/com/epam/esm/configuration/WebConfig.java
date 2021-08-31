package com.epam.esm.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@ComponentScan("com.epam.esm")
@EnableWebMvc
//@Profile("prod")
public class WebConfig implements WebMvcConfigurer {

    private static final Logger LOGGER = LogManager.getLogger(WebConfig.class);
    private static final String PROD_DATABASE_PROPERTIES_FILENAME = "prod_database.properties";
    private static final String PROPERTY_DRIVER_CLASSNAME = "com.mysql.jdbc.Driver";
    private static final String PROPERTY_URL = "CONNECTION_URL";
    private static final String PROPERTY_USER = "USER";
    private static final String PROPERTY_PASSWORD = "PASSWORD";

    @Bean
    ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/");
        resolver.setSuffix(".jsp");
        resolver.setContentType("text/html;charset=utf8");
        return resolver;
    }

    @Bean
    public HikariDataSource dataSource() {
        HikariConfig config = new HikariConfig();
        try (InputStream input = WebConfig.class.getClassLoader().getResourceAsStream(PROD_DATABASE_PROPERTIES_FILENAME)) {
            Properties properties = new Properties();
            properties.load(input);
            config.setMaximumPoolSize(10);
            config.setDriverClassName(PROPERTY_DRIVER_CLASSNAME);
            config.setJdbcUrl(properties.getProperty(PROPERTY_URL));
            config.setUsername(properties.getProperty(PROPERTY_USER));
            config.setPassword(properties.getProperty(PROPERTY_PASSWORD));
        } catch (IOException e) {
            LOGGER.error("Error read DB properties: " + e + " | Message: " + e.getMessage());
        }
        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbcTemplateProd() {
        return new JdbcTemplate(dataSource());
    }

}
