/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

import bin.common.DriverInfo;
import bin.common.DriverInfoManager;
import bin.common.mapper.JsonbMapper;
import bin.common.service.FactoryInstance;
import bin.common.vo.JsonbVo;
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
    @Before
    public void setUp() throws IOException {
        factory = FactoryInstance.INSTANCE.getSqlSessionFactory();
    }

    @After
    public void tearDown() throws SQLException {
    }

    @Test
    public void insertValueTest() throws IOException {
        JSONObject objRoot = new JSONObject();
        objRoot.put("key", "value0");
        objRoot.put("key1", "value1");
        objRoot.put("key2", "value2");
        SqlSessionFactory factory = FactoryInstance.INSTANCE.getSqlSessionFactory();
        try (SqlSession session = factory.openSession(true)) {
            JsonbMapper mapper = session.getMapper(JsonbMapper.class);
            int num = mapper.insertOne(JsonbVo.parse(4, objRoot));
            assertEquals(1, num);
            JsonbVo vo = mapper.selectJsonb(4);
            assertEquals(vo.id.intValue(), 4);
            assertEquals(vo.data.getValue(), objRoot.toJSONString());
        }
    }
}
