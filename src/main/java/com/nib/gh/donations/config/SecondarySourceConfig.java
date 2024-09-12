// package com.nib.gh.donations.config;

// import java.sql.Connection;
// import java.sql.DatabaseMetaData;
// import java.sql.SQLException;
// import java.util.HashMap;
// import java.util.Map;

// import javax.sql.DataSource;

// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.boot.jdbc.DataSourceBuilder;
// import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Primary;
// import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.orm.jpa.JpaTransactionManager;
// import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
// import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
// import org.springframework.transaction.PlatformTransactionManager;
// import org.springframework.transaction.annotation.EnableTransactionManagement;


// import jakarta.persistence.EntityManagerFactory;

// @Configuration
// @EnableTransactionManagement
// @EnableJpaRepositories(entityManagerFactoryRef = "secondEntityManagerFactory", transactionManagerRef = "secondTransactionManager", basePackages = {
//         "com.nib.gh.donations.esbrepository" })
// public class SecondarySourceConfig {
//  Logger logger = LogManager.getLogger(SecondarySourceConfig.class);

//     @Bean(name = "secondDataSource")
//     @ConfigurationProperties(prefix = "second.datasource")
//     public DataSource dataSource() {
//         return DataSourceBuilder.create().build();
//     }

//     @Bean(name = "secondEntityManagerFactory")
//     public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
//             @Qualifier("secondDataSource") DataSource dataSource) throws SQLException {

//                 logger.info("DATASOURCE " + dataSource);
//         if (dataSource != null) {
//             Connection connection = dataSource.getConnection();
//             DatabaseMetaData metaData = connection.getMetaData();
//             logger.info("SQL URL:: " + metaData.getURL());
//         }
//         LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//         emf.setDataSource(dataSource);
//         emf.setPackagesToScan("com.nib.gh.donations.esb");

//         HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//         vendorAdapter.setShowSql(true);
//         vendorAdapter.setGenerateDdl(false);
//         vendorAdapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");

//         emf.setJpaVendorAdapter(vendorAdapter);
//         emf.setJpaPropertyMap(jpaProperties());
//         return emf;
//     }

//     @Bean(name = "secondTransactionManager")
//     public PlatformTransactionManager transactionManager(
//             @Qualifier("secondEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
//         return new JpaTransactionManager(entityManagerFactory);
//     }

//     private Map<String, Object> jpaProperties() {
//         Map<String, Object> props = new HashMap<>();
//         props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//         props.put("hibernate.show_sql", "true");
//         props.put("hibernate.format_sql", "true");
//         return props;
//     }

//     @Primary
//     @Bean(name = "secondJdbcTemplate")
//     public JdbcTemplate jdbcTemplate(@Qualifier("secondDataSource") DataSource postgresSQL) {
//         return new JdbcTemplate(postgresSQL);
//     }

// }
