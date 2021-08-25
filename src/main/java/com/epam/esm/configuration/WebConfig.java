package com.epam.esm.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

@Configuration
@ComponentScan("com.epam.esm")
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private static final Logger LOGGER = LogManager.getLogger(WebConfig.class);
    private static final String PROPERTIES_FILENAME = "database.properties";
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
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        try (InputStream input = WebConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME)) {
            Properties properties = new Properties();
            properties.load(input);
            dataSource.setDriverClassName(PROPERTY_DRIVER_CLASSNAME);
            dataSource.setUrl(properties.getProperty(PROPERTY_URL));
            dataSource.setUsername(properties.getProperty(PROPERTY_USER));
            dataSource.setPassword(properties.getProperty(PROPERTY_PASSWORD));
        } catch (IOException e) {
            LOGGER.error("Error read DB properties: " + e + " | MESSAGE: " + e.getMessage());
        }
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.indentOutput(true);
        builder.dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return builder;
    }

}
