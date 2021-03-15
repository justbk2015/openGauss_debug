/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

import bin.common.DriverInfo;
import bin.common.DriverInfoManager;
import bin.common.mapper.BatchInsertMapper;
import bin.common.mapper.BlobMapper;
import bin.common.service.FactoryInstance;
import bin.common.vo.BatchInsertVo;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * description:this for JdbcBatchTest Class
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/3/15]
 * @since 2021/3/15
 */
public class MybatisProviderTest {
    private SqlSessionFactory factory = null;
    private SqlSession sqlSession;
    private BatchInsertMapper mapper;
    private static int[] autoIdCreateLock = new int[0];
    private static AtomicInteger autoId = null;
    @Before
    public void setUp() throws IOException {
        factory = FactoryInstance.INSTANCE.getSqlSessionFactory();
        sqlSession = factory.openSession(true);
        mapper = sqlSession.getMapper(BatchInsertMapper.class);
        mapper.createTable();
        initAutoId(mapper);
    }

    private static void initAutoId(BatchInsertMapper mapper) {
        if (autoId != null) {
            return;
        }
        synchronized (autoIdCreateLock) {
            if (autoId != null) {
                return;
            }
            autoId = new AtomicInteger(mapper.maxId());
        }
    }
    @After
    public void tearDown() throws SQLException {
        mapper.dropTable();
        sqlSession.close();
    }

    @Test
    public void test() {
        System.out.println("just a test!");
        BatchInsertVo vo = new BatchInsertVo();
        vo.id = autoId.getAndIncrement();
        vo.data = "this is a test " + vo.id;
        int num = mapper.add(vo);
        assertEquals(1, num);
    }
}
