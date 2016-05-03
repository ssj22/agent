package net.cloudamize.agent.config;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class JOOQConfiguration {

    @Bean
    public DSLContext masterContext(DataSource masterDataSource) throws SQLException {
        return DSL.using(masterDataSource, SQLDialect.POSTGRES);
    }

    @Bean
    public DSLContext slaveContext(DataSource slaveDataSource) throws SQLException {
        return DSL.using(slaveDataSource, SQLDialect.POSTGRES);
    }
}
