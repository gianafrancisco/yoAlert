/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package com.fransis;

import org.apache.tomcat.jdbc.pool.DataSource;
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
@Configuration
@EnableJpaRepositories("com.fransis.repository")
public class MemoryDBConfig {


    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;
    private final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;
    private final EntityManagerFactory entityManagerFactory;
    private final PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor;

    public MemoryDBConfig() {
        dataSource = getDataSource();
        entityManagerFactoryBean = entityManagerFactoryBean();
        entityManagerFactory = entityManagerFactoryBean.getObject();
        transactionManager = getTransactionManager(entityManagerFactory);
        persistenceExceptionTranslationPostProcessor = getPersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public DataSource dataSource() {
        return dataSource;
    }

    public DataSource getDataSource(){
        DataSource dataSource = new DataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUrl("jdbc:hsqldb:mem:.");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return transactionManager;
    }

    public PlatformTransactionManager getTransactionManager(EntityManagerFactory entityManagerFactory){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }


    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return persistenceExceptionTranslationPostProcessor;
    }

    public PersistenceExceptionTranslationPostProcessor getPersistenceExceptionTranslationPostProcessor(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(){
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean= new LocalContainerEntityManagerFactoryBean();

        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("com.fransis");
        entityManagerFactoryBean.setPersistenceUnitName("test");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        Properties ps = new Properties();
        ps.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        ps.put("hibernate.hbm2ddl.auto", "create-drop");
        ps.put("hibernate.archive.autodetection","class");
        ps.put("hibernate.show_sql","false");

        ps.put("hibernate.order_inserts", "true");
        ps.put("hibernate.order_updates", "true");
        ps.put("hibernate.jdbc.batch_size", "2");

        entityManagerFactoryBean.setJpaProperties(ps);
        entityManagerFactoryBean.afterPropertiesSet();
        return entityManagerFactoryBean;
    }


    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return entityManagerFactory;
    }
/*
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactoryBean().getObject();
    }
*/
}
