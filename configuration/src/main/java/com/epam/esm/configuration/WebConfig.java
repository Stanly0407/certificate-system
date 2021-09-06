package com.epam.esm.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan({"com.epam.esm.services", "com.epam.esm.repository", "com.epam.esm.configuration",
        "com.epam.esm.controllers", "com.epam.esm.entities"})
@EnableWebMvc
@PropertySource("classpath:prod_database.properties")
@PropertySource("classpath:dev_database.properties")
public class WebConfig implements WebMvcConfigurer {

    private static final int POOL_SIZE = 10;

    @Value("${DEV_DRIVER_CLASSNAME}")
    private String devDriverClassname;

    @Value("${DEV_CONNECTION_URL}")
    private String devUrl;

    @Value("${DEV_USER}")
    private String devUsername;

    @Value("${DEV_PASSWORD}")
    private String devPassword;

    @Value("${DRIVER_CLASSNAME}")
    private String prodDriverClassname;

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
        config.setMaximumPoolSize(POOL_SIZE);
        config.setDriverClassName(prodDriverClassname);
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
        config.setMaximumPoolSize(POOL_SIZE);
        config.setDriverClassName(devDriverClassname);
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

}
