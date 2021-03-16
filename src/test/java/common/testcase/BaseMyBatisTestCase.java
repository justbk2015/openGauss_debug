/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

package common.testcase;

import bin.common.mapper.BlobMapper;
import bin.common.service.FactoryInstance;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * description:this for BaseMyBatisTestCase Class
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/3/16]
 * @since 2021/3/16
 */
public abstract class BaseMyBatisTestCase<T> {
    protected int[] autoIdCreateLock = new int[0];
    protected AtomicInteger autoId = null;
    protected SqlSessionFactory factory = null;
    protected SqlSession sqlSession;
    protected T mapper;

    public abstract T getMapper();
    public abstract int getMaxId(T mapper);

    public  <T> T getMapper(Class<T> clz) {
        return sqlSession.getMapper(clz);
    }
    protected void initSession() {
        try {
            factory = FactoryInstance.INSTANCE.getSqlSessionFactory();
            sqlSession = factory.openSession(true);
            mapper = getMapper();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void closeSession() {
        sqlSession.close();
    }

    public void initAutoId(T mapper) {
        synchronized (autoIdCreateLock) {
            int maxId = getMaxId(mapper);
            autoId = new AtomicInteger(maxId);
        }
    }

    public int getNextId() {
        return autoId.getAndIncrement();
    }
}
