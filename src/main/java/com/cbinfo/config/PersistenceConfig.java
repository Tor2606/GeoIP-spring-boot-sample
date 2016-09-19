package com.cbinfo.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;
import java.util.Properties;

/**
 * Created by islabukhin on 16.09.16.
 */
@Configuration
@EnableTransactionManagement
public class PersistenceConfig {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.driver}")
    String driver;


    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceConfig.class);

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(Boolean.TRUE);//log into console
        adapter.setDatabase(Database.POSTGRESQL);
        adapter.setGenerateDdl(Boolean.FALSE);
        adapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");
        return adapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setJpaVendorAdapter(jpaVendorAdapter);
        factory.setPackagesToScan("com.cbinfo.model");
        /*factory.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
        factory.setValidationMode(ValidationMode.NONE);*/
        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "none");
        jpaProperties.setProperty("hibernate.show_sql", Boolean.FALSE.toString());
        jpaProperties.setProperty("testWhileIdle", Boolean.TRUE.toString());
        jpaProperties.setProperty("hibernate.show_sql", Boolean.FALSE.toString());

        factory.setJpaProperties(jpaProperties);
        return factory;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        LOGGER.info("Connecting to PostgreSQL on localhost");

        /*JndiDataSourceLookup lookup = new JndiDataSourceLookup();
        return lookup.getDataSource("jdbc/CustomerSupport"); - what about this variant?*/

        DataSource ds = new DataSource();
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setUrl(url);
        ds.setDriverClassName(driver);
/*
        If true connections will be validated by the idle connection evictor (if any).
        If the validation fails, the connection is destroyed and removed from the pool
*/
        ds.setTestWhileIdle(Boolean.TRUE);
        ds.setValidationQuery("SELECT 1");
        ds.setValidationInterval(30);
        return ds;
    }
}
