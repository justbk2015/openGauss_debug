/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

import bin.common.DriverInfo;
import bin.common.DriverInfoManager;
import bin.common.mapper.JsonbMapper;
import bin.common.service.FactoryInstance;
import bin.common.vo.JsonbVo;
import com.huawei.shade.com.alibaba.fastjson.JSONArray;
import com.huawei.shade.com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * description:this for JsonbTest Class
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/3/9]
 * @since 2021/3/9
 */
public class JsonbTest {
    private SqlSessionFactory factory = null;
    private SqlSession sqlSession;
    private JsonbMapper jsonbMapper;
    private static int[] autoIdCreateLock = new int[0];
    private static AtomicInteger autoId = null;
    @Before
    public void setUp() throws IOException {
        factory = FactoryInstance.INSTANCE.getSqlSessionFactory();
        sqlSession = factory.openSession(true);
        jsonbMapper = sqlSession.getMapper(JsonbMapper.class);
        jsonbMapper.createTable();
        initAutoId(jsonbMapper);
        initData();
    }

    private static void initAutoId(JsonbMapper jsonbMapper) {
        if (autoId != null) {
            return;
        }
        synchronized (autoIdCreateLock) {
            if (autoId != null) {
                return;
            }
            autoId = new AtomicInteger(jsonbMapper.selectMaxId());
        }
    }
    @After
    public void tearDown() throws SQLException {
//        jsonbMapper.dropTable();
        sqlSession.close();
    }

    @Test
    public void insertValueTest() throws IOException {
        JSONObject objRoot = new JSONObject();
        objRoot.put("key", "value0");
        objRoot.put("key1", "value1");
        objRoot.put("key2", "value2");
        int newId = autoId.getAndIncrement();
        int num = jsonbMapper.insertOne(JsonbVo.parse(newId, objRoot));
        assertEquals(1, num);
        JsonbVo vo = jsonbMapper.selectJsonb(newId);
        assertEquals(vo.id.intValue(), newId);
        assertEquals(vo.data.getValue(), objRoot.toJSONString());
    }

    private void initData() {
        JSONObject rootKey = new JSONObject();
        rootKey.put("key", "value");
        jsonbMapper.insertOne(JsonbVo.parse(autoId.getAndIncrement(), rootKey));

        JSONArray childArr = new JSONArray();
        childArr.add(rootKey.clone());
        childArr.add(rootKey.clone());
        rootKey.put("keyArr", childArr);
        jsonbMapper.insertOne(JsonbVo.parse(autoId.getAndIncrement(), rootKey));
    }
}
