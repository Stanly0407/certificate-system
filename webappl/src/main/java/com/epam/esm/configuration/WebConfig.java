package com.epam.esm.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan({"com.epam.esm.service", "com.epam.esm.repository", "com.epam.esm.configuration",
        "com.epam.esm.controllers", "com.epam.esm.domain"})
@EnableWebMvc
@PropertySource("classpath:prod_database.properties")
@PropertySource("classpath:dev_database.properties")
public class WebConfig implements WebMvcConfigurer {

    private static final Logger LOGGER = LogManager.getLogger(WebConfig.class);
    private static final String DRIVER_CLASSNAME = "com.mysql.jdbc.Driver";
    @Value("${DEV_CONNECTION_URL}")
    private String devUrl;
    @Value("${DEV_USER}")
    private String devUsername;
    @Value("${DEV_PASSWORD}")
    private String devPassword;
    @Value("${CONNECTION_URL}")
    private String prodUrl;
    @Value("${USER}")
    private String prodUsername;
    @Value("${PASSWORD}")
    private String prodPassword;


    @Bean
    ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/");
        resolver.setSuffix(".jsp");
        resolver.setContentType("text/html;charset=utf8");
        return resolver;
    }

    @Profile("prod")
    @Bean
    public HikariDataSource dataSourceProd() {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(10);
        config.setDriverClassName(DRIVER_CLASSNAME);
        config.setJdbcUrl(prodUrl);
        config.setUsername(prodUsername);
        config.setPassword(prodPassword);
        return new HikariDataSource(config);
    }

    @Profile("prod")
    @Bean
    public JdbcTemplate jdbcTemplateProd() {
        return new JdbcTemplate(dataSourceProd());
    }

    @Profile("dev")
    @Bean
    public HikariDataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(10);
        config.setDriverClassName(DRIVER_CLASSNAME);
        config.setJdbcUrl(devUrl);
        config.setUsername(devUsername);
        config.setPassword(devPassword);
        return new HikariDataSource(config);
    }

    @Profile("dev")
    @Bean
    public JdbcTemplate jdbcTemplateDev() {
        return new JdbcTemplate(dataSource());
    }


//    @Bean
//    BeanNameUrlHandlerMapping beanNameUrlHandlerMapping() {
//        return new BeanNameUrlHandlerMapping();
//    }

}
