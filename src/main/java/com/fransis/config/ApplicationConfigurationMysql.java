/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package com.fransis.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

/**
 * Created by francisco on 04/12/2015.
 */
//@Configuration
//@EnableJpaRepositories("com.fransis.repository")
class ApplicationConfigurationMysql extends ApplicationConfiguration {

    @Value("${spring.datasource.driverClassName:'com.mysql.jdbc.Driver'}")
    private String driverClassName;
    @Value("${spring.datasource.url:'jdbc:mysql://localhost:3306/alertas'}")
    private String url;
    @Value("${spring.datasource.username:root}")
    private String username;
    @Value("${spring.datasource.password:123456}")
    private String password;

    private DataSource dataSource;
    private PlatformTransactionManager transactionManager;
    private LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;
    private EntityManagerFactory entityManagerFactory;
    private PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor;
    private boolean init = false;
/*
    public ApplicationConfigurationMysql(
            @Value("${spring.datasource.driverClassName:'com.mysql.jdbc.Driver'}") String driverClassName,
            @Value("${spring.datasource.url:'jdbc:mysql://localhost:3306/db'}") String url,
            @Value("${spring.datasource.username:root}") String username,
            @Value("${spring.datasource.password:123456}") String password
    ) {
        this.driverClassName = driverClassName;
        this.url = url;
        this.username = username;
        this.password = password;
        dataSource = getDataSource();
        entityManagerFactoryBean = entityManagerFactoryBean();
        entityManagerFactory = entityManagerFactoryBean.getObject();
        transactionManager = getTransactionManager(entityManagerFactory);
        persistenceExceptionTranslationPostProcessor = getPersistenceExceptionTranslationPostProcessor();
    }
*/
    private void init(){
        init = true;
        dataSource = getDataSource();
        entityManagerFactoryBean = entityManagerFactoryBean();
        entityManagerFactory = entityManagerFactoryBean.getObject();
        transactionManager = getTransactionManager(entityManagerFactory);
        persistenceExceptionTranslationPostProcessor = getPersistenceExceptionTranslationPostProcessor();
    }
    @Bean
    @Override
    public DataSource dataSource() {
        if(!init) {
            init();
        }
        return dataSource;
    }

    private DataSource getDataSource(){
        DataSource dataSource = new DataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setTestOnBorrow(true);
        dataSource.setValidationQuery("SELECT 1");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        if(!init) {
            init();
        }
        return transactionManager;
    }

    private PlatformTransactionManager getTransactionManager(EntityManagerFactory entityManagerFactory){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        if(!init) {
            init();
        }
        return persistenceExceptionTranslationPostProcessor;
    }

    private PersistenceExceptionTranslationPostProcessor getPersistenceExceptionTranslationPostProcessor(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Override
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(){
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean= new LocalContainerEntityManagerFactoryBean();

        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan("com.fransis");
        entityManagerFactoryBean.setPersistenceUnitName("emsPU");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        Properties ps = new Properties();
        ps.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        ps.put("hibernate.hbm2ddl.auto", "update");
        ps.put("hibernate.archive.autodetection","class");
        ps.put("hibernate.show_sql","false");

        entityManagerFactoryBean.setJpaProperties(ps);
        entityManagerFactoryBean.afterPropertiesSet();
        return entityManagerFactoryBean;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        if(!init) {
            init();
        }
        return entityManagerFactory;
    }

}

