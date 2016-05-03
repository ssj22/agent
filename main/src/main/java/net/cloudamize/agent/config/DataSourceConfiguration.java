package net.cloudamize.agent.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:/application.properties")
public class DataSourceConfiguration {

    @Autowired
    private Environment env;

    @Bean
    @Primary
    public DataSource masterDataSource() {
        String masterDriverClass = env.getProperty("db.master.driver");
        String masterUrl = env.getProperty("db.master.url");
        String masterPassword = env.getProperty("db.master.password");
        String masterUsername = env.getProperty("db.master.username");

        return buildDataSource(masterDriverClass, masterUrl, masterPassword, masterUsername);
    }

    @Bean
    public DataSource slaveDataSource() {
        String slaveDriverClass = env.getProperty("db.slave.driver");
        String slaveUrl = env.getProperty("db.slave.url");
        String slavePassword = env.getProperty("db.slave.password");
        String slaveUsername = env.getProperty("db.slave.username");

        return buildDataSource(slaveDriverClass, slaveUrl, slavePassword, slaveUsername);
    }

    private DataSource buildDataSource(String driverClass, String url, String password, String username) {
        org.apache.tomcat.jdbc.pool.DataSource datasource = new org.apache.tomcat.jdbc.pool.DataSource();
        datasource.setDriverClassName(driverClass);
        datasource.setUrl(url);
        datasource.setPassword(password);
        datasource.setUsername(username);
        datasource.setMaxActive(20);//important
        datasource.setMinIdle(4);
        datasource.setMaxIdle(4);//release time, important
        datasource.setInitialSize(4);//skip the warm up, we have ram
        return datasource;
    }
}
