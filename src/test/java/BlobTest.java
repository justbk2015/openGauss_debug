/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

import bin.common.mapper.BlobMapper;
import bin.common.vo.BlobVo;
import common.testcase.BaseMyBatisTestCase;
import common.utils.SecurityUtils;
import org.apache.ibatis.io.Resources;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * description:this for BlobTest Class
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/3/11]
 * @since 2021/3/11
 */
public class BlobTest extends BaseMyBatisTestCase<BlobMapper> {
    @Before
    public void setUp() throws IOException {
        initSession();
        mapper.dropTable();
        mapper.createTable();
        initAutoId(mapper);
    }

    @After
    public void tearDown() throws SQLException {
        mapper.dropTable();
        closeSession();
    }

    @Test
    public void queryTest() throws SQLException {
        List<BlobVo> result = mapper.selectBlobAll();
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
        int insert = mapper.insertBlobOne(blobVo);
        assertEquals(1, insert);

        BlobVo queryBlob = mapper.selectBlobById(blobVo.id);
        assertNotNull(queryBlob);
        assertEquals(blobVo.id, queryBlob.id);
        assertEquals(blobVo.descdata, queryBlob.descdata);
        assertTrue(queryBlob.data instanceof BlobVo.MyBlob);
        BlobVo.MyBlob queryMyBlob = (BlobVo.MyBlob) queryBlob.data;
        String newFile = queryMyBlob.fileName;
        File srcNewFile = new File(newFile);
        assertEquals(srcNewFile.length(), srcFile.length());
        try {
            new SecurityUtils().compareFile(srcFile.getAbsolutePath(), newFile);
        } catch (Exception e) {
            fail("compare file failed!");
        }
    }

    @Override
    public BlobMapper getMapper() {
        return getMapper(BlobMapper.class);
    }

    @Override
    public int getMaxId(BlobMapper mapper) {
        return mapper.selectMaxId();
    }
}
