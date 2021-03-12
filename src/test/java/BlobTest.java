/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

import bin.common.mapper.BlobMapper;
import bin.common.mapper.JsonbMapper;
import bin.common.service.FactoryInstance;
import bin.common.vo.BlobVo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.misc.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * description:this for BlobTest Class
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/3/11]
 * @since 2021/3/11
 */
public class BlobTest {
    private SqlSessionFactory factory = null;
    private SqlSession sqlSession;
    private BlobMapper blobMapper;
    private static int[] autoIdCreateLock = new int[0];
    private static AtomicInteger autoId = null;
    @Before
    public void setUp() throws IOException {
        factory = FactoryInstance.INSTANCE.getSqlSessionFactory();
        sqlSession = factory.openSession(true);
        blobMapper = sqlSession.getMapper(BlobMapper.class);
//        blobMapper.createTable();
        initAutoId(blobMapper);
    }

    private static void initAutoId(BlobMapper mapper) {
        if (autoId != null) {
            return;
        }
        synchronized (autoIdCreateLock) {
            if (autoId != null) {
                return;
            }
            autoId = new AtomicInteger(mapper.selectMaxId());
        }
    }
    @After
    public void tearDown() throws SQLException {
//        jsonbMapper.dropTable();
        sqlSession.close();
    }

    @Test
    public void queryTest() throws SQLException {
        List<BlobVo> result = blobMapper.selectBlobAll();
        assertEquals(0, result.size());
    }

    @Test
    public void insertAndQueryTest() throws SQLException {
        String testFile = "database.properties";
        File srcFile = null;
        try {
            srcFile = Resources.getResourceAsFile(testFile);
        } catch (IOException e) {
            fail("can\'t find file!");
        }
        BlobVo blobVo = new BlobVo();
        blobVo.id = autoId.getAndIncrement();
        blobVo.descdata = new String("this is simple test:" + blobVo.id);
        blobVo.data = new BlobVo.MyBlob(srcFile.getAbsolutePath());
        int insert = blobMapper.insertBlobOne(blobVo);
        assertEquals(1, insert);

        BlobVo queryBlob = blobMapper.selectBlobById(blobVo.id);
        assertNotNull(queryBlob);
        assertEquals(blobVo.id, queryBlob.id);
        assertEquals(blobVo.descdata, queryBlob.descdata);
        assertTrue(queryBlob.data instanceof BlobVo.MyBlob);
        BlobVo.MyBlob queryMyBlob = (BlobVo.MyBlob) queryBlob.data;
        String newFile = queryMyBlob.fileName;
        File srcNewFile = new File(newFile);
        assertEquals(srcNewFile.length(), srcFile.length());
    }
}
