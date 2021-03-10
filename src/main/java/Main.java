/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

import bin.common.DriverInfo;
import bin.common.DriverInfoManager;
import bin.common.mapper.JsonbMapper;
import bin.common.service.FactoryInstance;
import bin.common.vo.JsonbVo;
import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * description:this for Main Class
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/3/9]
 * @since 2021/3/9
 */
public class Main {
    public static void main(String ... args) throws IOException {
        SqlSessionFactory sqlSessionFactory = FactoryInstance.INSTANCE.getSqlSessionFactory();
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            JsonbMapper mapper = session.getMapper(JsonbMapper.class);
            JsonbVo vo = mapper.selectJsonb(2);
            System.out.println("vo=" + vo.toString());

            List<JsonbVo> allVos = mapper.selectJsonbAll();
            allVos.stream().forEach(curVo -> {
                System.out.println("vos=" + curVo.toString());
            });

            List<JsonbVo> allVos1 = session.selectList("selectJsonbAll");
            allVos1.stream().forEach(curVo1 -> {
                System.out.println("vos1=" + curVo1.toString());
            });

            Map<String, Object> allVos2 = session.selectMap("selectJsonbAll", "id");
            System.out.println(allVos2.size());

        }
        System.out.println("hello, world!");
    }
}
