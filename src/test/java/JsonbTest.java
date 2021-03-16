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
import common.testcase.BaseMyBatisTestCase;
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
public class JsonbTest extends BaseMyBatisTestCase<JsonbMapper> {
    @Override
    public JsonbMapper getMapper() {
        return getMapper(JsonbMapper.class);
    }

    @Override
    public int getMaxId(JsonbMapper mapper) {
        return mapper.selectMaxId();
    }

    @Before
    public void setUp() throws IOException {
        initSession();
        mapper.dropTable();
        mapper.createTable();
        initAutoId(mapper);
        initData();
    }

    @After
    public void tearDown() throws SQLException {
        mapper.dropTable();
        closeSession();
    }

    @Test
    public void insertValueTest() throws IOException {
        JSONObject objRoot = new JSONObject();
        objRoot.put("key", "value0");
        objRoot.put("key1", "value1");
        objRoot.put("key2", "value2");
        int newId = autoId.getAndIncrement();
        int num = mapper.insertOne(JsonbVo.parse(newId, objRoot));
        assertEquals(1, num);
        JsonbVo vo = mapper.selectJsonb(newId);
        assertEquals(vo.id.intValue(), newId);
        assertEquals(vo.data.getValue(), objRoot.toJSONString());
    }

    private void initData() {
        JSONObject rootKey = new JSONObject();
        rootKey.put("key", "value");
        mapper.insertOne(JsonbVo.parse(autoId.getAndIncrement(), rootKey));

        JSONArray childArr = new JSONArray();
        childArr.add(rootKey.clone());
        childArr.add(rootKey.clone());
        rootKey.put("keyArr", childArr);
        mapper.insertOne(JsonbVo.parse(autoId.getAndIncrement(), rootKey));
    }
}
