/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */

import bin.common.DriverInfo;
import bin.common.DriverInfoManager;
import bin.common.Logger;
import common.testcase.BaseJdbcTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Title: the MyFirstTest class
 * <p>
 * Description:
 * <p>
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019.
 *
 * @author z00588921
 * @version [DataStudio 1.0.0, 2021/1/28]
 * @since 2021/1/28
 */
public class MyFirstTest extends BaseJdbcTestCase {
    public static DriverInfo info = DriverInfoManager.getInfo();
    public static IntConvert convert = new IntConvert();
    public static SimpleParseRs sRs = new SimpleParseRs();
    private Connection conn;

    @Before
    public void setUp() throws SQLException {
        conn = DriverInfoManager.getConnection(info);
    }

    @After
    public void tearDown() throws SQLException {
        conn.close();
        conn = null;
    }

    @Test
    public void testConnect() throws SQLException {
        try (Connection conn = DriverInfoManager.getConnection(info)) {
            Integer count = convert.convert(executeAndGetOne("select count(*) from pg_class;", sRs));
            Logger.info("pg_class count = " + count);
        }
    }

    public void testReadonly() throws SQLException {
        try (Connection conn = DriverInfoManager.getConnection(info)) {
            conn.setReadOnly(true);
        }
    }
    public void testReadOnlySql() throws SQLException {
        String sql = "SET SESSION CHARACTERISTICS AS TRANSACTION READ ONLY;";
        try (Connection conn = DriverInfoManager.getConnection(info)) {
//            executeNoResult(conn, "begin;");
            executeSql(conn, sql);
//            executeNoResult(conn, "end;");
        }
    }

    public void testReadOnlySql_1() throws SQLException {
        String sql = "ALTER SYSTEM set default_transaction_read_only=true";
//        String sql = "set transaction_read_only to true;";
        try (Connection conn = DriverInfoManager.getConnection(info)) {
//            executeNoResult(conn, "begin;");
            executeSql(conn, sql);
//            executeNoResult(conn, "end;");
        }
    }

    @Override
    public Connection getConn() {
        return conn;
    }

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public String getTableColumns4Create() {
        return null;
    }
}
