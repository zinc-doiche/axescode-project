package com.github.axescode.mybatis;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class HikariDataSourceFactory implements DataSourceFactory {
    private static final int IDLE = 10;
    private static final int MAX_POOL_SIZE = 10;
    private static final Long MAX_LIFE_TIME = 58000L;
    private static final HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;

    @Override
    public void setProperties(Properties props) {
        config.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
        config.setDataSourceProperties(props);
        config.setMinimumIdle(IDLE);
        config.setMaximumPoolSize(MAX_POOL_SIZE);
        config.setMaxLifetime(MAX_LIFE_TIME);

        dataSource = new HikariDataSource(config);
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    public static void closeDataSource() {
        dataSource.close();
    }
}
