/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

import bin.common.DriverInfo;
import bin.common.DriverInfoManager;
import bin.common.mapper.BatchInsertMapper;
import bin.common.mapper.BlobMapper;
import bin.common.service.FactoryInstance;
import bin.common.vo.BatchInsertVo;
import common.testcase.BaseMyBatisTestCase;
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
public class MybatisProviderTest extends BaseMyBatisTestCase<BatchInsertMapper> {
    @Before
    public void setUp() throws IOException {
        initSession();
        mapper.createTable();
        initAutoId(mapper);
    }

    @After
    public void tearDown() throws SQLException {
        mapper.dropTable();
        closeSession();
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

    @Override
    public BatchInsertMapper getMapper() {
        return getMapper(BatchInsertMapper.class);
    }

    @Override
    public int getMaxId(BatchInsertMapper mapper) {
        return mapper.maxId();
    }
}
