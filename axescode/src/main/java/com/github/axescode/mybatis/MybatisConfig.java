package com.github.axescode.mybatis;

import com.github.axescode.core.generator.PlacedGeneratorVO;
import com.github.axescode.core.player.PlayerVO;
import com.github.axescode.mybatis.mapper.PlacedGeneratorMapper;
import com.github.axescode.mybatis.mapper.PlayerMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;

public class MybatisConfig {
    private static final int IDLE = 30;
    private static final int MAX_POOL_SIZE = 30;
    private static final Long MAX_LIFE_TIME = 58000L;

    private static SqlSessionFactory sqlSessionFactory;

    static {
        String resource = "mybatis/config.xml";
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    static {
//        HikariConfig hikari = new HikariConfig();
//
//        hikari.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
//        hikari.addDataSourceProperty("serverName", "localhost");
//        hikari.addDataSourceProperty("port", 3306);
//        hikari.addDataSourceProperty("databaseName", "axescode");
//        hikari.addDataSourceProperty("user", "root");
//        hikari.addDataSourceProperty("password", 1234);
//
//        hikari.setMinimumIdle(IDLE);
//        hikari.setMaximumPoolSize(MAX_POOL_SIZE);
//        hikari.setMaxLifetime(MAX_LIFE_TIME);
//
//        DataSource dataSource = new HikariDataSource(hikari);
//        TransactionFactory transactionFactory = new JdbcTransactionFactory();
//        Environment environment = new Environment("development", transactionFactory, dataSource);
//        Configuration configuration = new Configuration(environment);
//
//        configuration.setMapUnderscoreToCamelCase(true);
//        configuration.getTypeAliasRegistry().registerAlias("playerVO", PlayerVO.class);
//        configuration.getTypeAliasRegistry().registerAlias("placedGeneratorVO", PlacedGeneratorVO.class);
//        configuration.addMapper(PlayerMapper.class);
//        configuration.addMapper(PlacedGeneratorMapper.class);
//
//        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
//    }

    public static void init() {
        // Perform initialization if needed
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}