package com.github.axescode.core;

import com.github.axescode.mybatis.MybatisConfig;
import org.apache.ibatis.session.SqlSession;

import java.util.function.Consumer;


public abstract class AbstractDAO<D extends AbstractDAO<D>> implements AutoCloseable {
    protected final SqlSession sqlSession;

    protected AbstractDAO(boolean autoCommit) {
        sqlSession = MybatisConfig.getSqlSessionFactory().openSession(true);
    }

    @Override
    public void close() {
        sqlSession.close();
    }
}