package ru.savuri.webprak.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource({"classpath:application.properties"})
@EnableTransactionManagement
public class HibernateDatabaseConfig {
    @Value("${db_url}")
    private String db_url;
    @Value("${db_username}")
    private String db_username;
    @Value("${db_password}")
    private String db_password;
    @Value("${db_driver}")
    private String db_driver;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(oraDataSource());
        sessionFactory.setPackagesToScan("ru.savuri.webprak.model.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public DataSource oraDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(db_driver);
        dataSource.setUrl(db_url);
        dataSource.setUsername(db_username);
        dataSource.setPassword(db_password);

        return dataSource;
    }

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "hibernate.hbm2ddl.auto", "update");
        hibernateProperties.setProperty(
                "hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
        hibernateProperties.setProperty("connection_pool_size", "1");

        return hibernateProperties;
    }
}