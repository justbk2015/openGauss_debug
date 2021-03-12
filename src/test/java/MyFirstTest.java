/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */

import bin.common.DriverInfo;
import bin.common.DriverInfoManager;
import bin.common.Logger;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
public class MyFirstTest {
    public static DriverInfo info = DriverInfoManager.getInfo("db5");
    public static TypeConvert convert = new TypeConvert();
    public static SimpleParseRs sRs = new SimpleParseRs();
    @Test
    public void testConnect() throws SQLException {
        try (Connection conn = DriverInfoManager.getConnection(info)) {
            Integer count = convert.convert(executeWithResult(conn, "select count(*) from pg_class;", sRs));
            Logger.info("pg_class count = " + count);
        }
    }

//    @Test
    public void testReadonly() throws SQLException {
        try (Connection conn = DriverInfoManager.getConnection(info)) {
            conn.setReadOnly(true);
        }
    }
//    @Test
    public void testReadOnlySql() throws SQLException {
        String sql = "SET SESSION CHARACTERISTICS AS TRANSACTION READ ONLY;";
        try (Connection conn = DriverInfoManager.getConnection(info)) {
//            executeNoResult(conn, "begin;");
            executeNoResult(conn, sql);
//            executeNoResult(conn, "end;");
        }
    }

//    @Test
    public void testReadOnlySql_1() throws SQLException {
        String sql = "ALTER SYSTEM set default_transaction_read_only=true";
//        String sql = "set transaction_read_only to true;";
        try (Connection conn = DriverInfoManager.getConnection(info)) {
//            executeNoResult(conn, "begin;");
            executeNoResult(conn, sql);
//            executeNoResult(conn, "end;");
        }
    }

    public void executeNoResult(Connection conn, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.execute();
        }
    }

    public<T> T executeWithResult(Connection conn, String sql, ParseResultSet<T> parse) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return parse.parse(rs);
            }
        }
    }

    public <T> List<T> executeWithResults(Connection conn, String sql, ParseResultSet<T> parse) throws SQLException {
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return parse.parseList(rs);
            }
        }
    }
    public static interface ParseResultSet<T> {
        T parse(ResultSet rs) throws SQLException;
        default List<T> parseList(ResultSet rs) throws SQLException {
            List<T> results = new ArrayList<>(1);
            while (rs.next()) {
                results.add(parse(rs));
            }
            return results;
        }
    }

    public static class SimpleParseRs implements ParseResultSet<Object> {
        private Object parseResult = null;
        private boolean isParse = false;
        @Override
        public Object parse(ResultSet rs) throws SQLException {
            if (isParse) {
                return parseResult;
            }
            isParse = true;
            if (rs.next()) {
                parseResult = rs.getObject(1);
            }
            return parseResult;
        }
    }

    public static class TypeConvert {
        public Integer convert(Object obj) throws SQLException {
            if (obj instanceof Integer) {
                return (Integer) obj;
            }
            if (obj instanceof Long) {
                return ((Long) obj).intValue();
            }
            throw new SQLException("type not match!");
        }
    }
}
