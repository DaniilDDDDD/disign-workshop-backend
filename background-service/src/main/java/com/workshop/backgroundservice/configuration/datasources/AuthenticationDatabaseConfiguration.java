package com.workshop.backgroundservice.configuration.datasources;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@PropertySource("classpath:application.yml")
@EnableJpaRepositories(
        basePackages = "com.workshop.backgroundservice.repository.authentication",
        entityManagerFactoryRef = "authenticationEntityManager",
        transactionManagerRef = "authenticationTransactionManager"
)
public class AuthenticationDatabaseConfiguration {

    @Autowired
    private Environment environment;


    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean authenticationEntityManager() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(authenticationDataSource());
        em.setPackagesToScan(new String[] { "com.workshop.backgroundservice.model.user" });

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.ddl-auto", environment.getProperty("postgres.auth.ddl-auto"));
        properties.put("hibernate.dialect", environment.getProperty("postgres.auth.dialect"));

        em.setJpaPropertyMap(properties);

        return em;
    }


    @Bean
    @Primary
    public DataSource authenticationDataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUrl(environment.getProperty("postgres.auth.url"));
        dataSource.setUsername(environment.getProperty("postgres.auth.username"));
        dataSource.setPassword(environment.getProperty("postgres.auth.password"));
        dataSource.setSchema("main");

        return dataSource;
    }


    @Bean
    @Primary
    public PlatformTransactionManager authenticationTransactionManager() {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(authenticationEntityManager().getObject());
        return transactionManager;
    }

}
