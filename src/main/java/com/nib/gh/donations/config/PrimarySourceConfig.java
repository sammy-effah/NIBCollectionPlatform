// package com.nib.gh.donations.config;

// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.boot.jdbc.DataSourceBuilder;
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
// import javax.sql.DataSource;
// import jakarta.persistence.EntityManagerFactory;
// import java.sql.SQLException;
// import java.util.HashMap;
// import java.util.Map;

// @Configuration
// @EnableTransactionManagement
// @EnableJpaRepositories(basePackages = "com.nib.gh.donations.repository", entityManagerFactoryRef = "primaryEntityManagerFactory", transactionManagerRef = "primaryTransactionManager")
// public class PrimarySourceConfig {

//     Logger logger = LogManager.getLogger(PrimarySourceConfig.class);

//     @Primary
//     @Bean(name = "primaryDataSource")
//     @ConfigurationProperties(prefix = "spring.datasource")
//     public DataSource dataSource() {
//         return DataSourceBuilder.create().build();
//     }

//     @Primary
//     @Bean(name = "primaryEntityManagerFactory")
//     public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//             @Qualifier("primaryDataSource") DataSource dataSource) throws SQLException {

//         LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//         emf.setDataSource(dataSource);
//         emf.setPackagesToScan("com.nib.gh.donations.model");

//         HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//         vendorAdapter.setShowSql(true);
//         vendorAdapter.setGenerateDdl(false);
//         vendorAdapter.setDatabasePlatform("org.hibernate.dialect.SQLServerDialect");

//         emf.setJpaVendorAdapter(vendorAdapter);
//         emf.setJpaPropertyMap(jpaProperties());
//         return emf;
//     }

//     @Primary
//     @Bean(name = "primaryTransactionManager")
//     public PlatformTransactionManager transactionManager(
//             @Qualifier("primaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
//         return new JpaTransactionManager(entityManagerFactory);
//     }

//     private Map<String, Object> jpaProperties() {
//         Map<String, Object> props = new HashMap<>();
//         props.put("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
//         props.put("hibernate.show_sql", "true");
//         props.put("hibernate.format_sql", "true");
//         return props;
//     }

//     @Primary
//     @Bean(name = "primaryJdbcTemplate")
//     public JdbcTemplate jdbcTemplate(@Qualifier("primaryDataSource") DataSource dsMSSQL) {
//         return new JdbcTemplate(dsMSSQL);
//     }
// }
