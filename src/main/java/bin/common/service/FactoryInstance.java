/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

package bin.common.service;

import bin.common.DriverInfo;
import bin.common.DriverInfoManager;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * description:this for FactoryInstance Class
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/3/9]
 * @since 2021/3/9
 */
public class FactoryInstance {
    public static final String MYBATIS_CONF = "mybatis-config.xml";
    public static FactoryInstance INSTANCE = new FactoryInstance();
    private int[] sqlSessionLock = new int[0];
    private volatile SqlSessionFactory sqlSessionFactory = null;

    private FactoryInstance() {

    }
    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        if (sqlSessionFactory != null) {
            return sqlSessionFactory;
        }
        synchronized (sqlSessionLock) {
            if (sqlSessionFactory != null) {
                return sqlSessionFactory;
            }
            DriverInfo info = DriverInfoManager.getInfo();
            InputStream inputStream = Resources.getResourceAsStream(MYBATIS_CONF);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, info.getProperties());
            return sqlSessionFactory;
        }
    }
}
